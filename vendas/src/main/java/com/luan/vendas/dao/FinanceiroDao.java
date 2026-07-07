package com.luan.vendas.dao;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.Financeiro;

import jakarta.transaction.SystemException;

public class FinanceiroDao {

	private static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public boolean salvarHibernate(Financeiro financeiro) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.persist(financeiro);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(Financeiro financeiro) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.merge(financeiro);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean excluirHibernate(Financeiro financeiro) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			Financeiro financeiroEncontrado = session.find(Financeiro.class, financeiro.getId());

			if (financeiroEncontrado == null) {
				transaction.rollback();
				return false;
			}

			session.remove(financeiroEncontrado);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao excluir financeiro: " + e.getMessage());
			return false;
		}
	}

	public List<Financeiro> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery(
				"select distinct f from Financeiro f "
					+ "left join fetch f.fornecedor "
					+ "left join fetch f.cliente "
					+ "left join fetch f.tipoConta "
					+ "left join fetch f.formaPagamento "
					+ "order by f.id",
				Financeiro.class
			).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<Financeiro> pesquisarHibernate(String termo) {
		List<Financeiro> financeiros = pesquisarHibernate();
		if (termo == null || termo.trim().isEmpty()) {
			return financeiros;
		}

		String textoBusca = termo.trim().toLowerCase(Locale.ROOT);
		return financeiros.stream()
			.filter(financeiro -> {
				String idTexto = String.valueOf(financeiro.getId());
				String dataTexto = financeiro.getData_conta() != null ? financeiro.getData_conta().format(UI_DATE_FORMATTER) : "";
				String fornecedorTexto = financeiro.getFornecedor() != null ? financeiro.getFornecedor().getNome_fantasia() : "";
				String clienteTexto = financeiro.getCliente() != null ? financeiro.getCliente().getNome() : "";
				String tipoContaTexto = financeiro.getTipoConta() != null ? financeiro.getTipoConta().getDescricao() : "";
				String formaPagamentoTexto = financeiro.getFormaPagamento() != null ? financeiro.getFormaPagamento().getNome() : "";
				return idTexto.contains(textoBusca)
					|| dataTexto.contains(textoBusca)
					|| fornecedorTexto.toLowerCase(Locale.ROOT).contains(textoBusca)
					|| clienteTexto.toLowerCase(Locale.ROOT).contains(textoBusca)
					|| tipoContaTexto.toLowerCase(Locale.ROOT).contains(textoBusca)
					|| formaPagamentoTexto.toLowerCase(Locale.ROOT).contains(textoBusca);
			})
			.collect(Collectors.toList());
	}

	public Financeiro pesquisarHibernate(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery(
				"select distinct f from Financeiro f "
					+ "left join fetch f.fornecedor "
					+ "left join fetch f.cliente "
					+ "left join fetch f.tipoConta "
					+ "left join fetch f.formaPagamento "
					+ "where f.id = :id",
				Financeiro.class
			).setParameter("id", id).uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}
}