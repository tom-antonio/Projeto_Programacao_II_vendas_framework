package com.luan.vendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.Financeiro;

import jakarta.transaction.SystemException;

public class FinanceiroDao {

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
			return session.createQuery("FROM Financeiro", Financeiro.class).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<Financeiro> pesquisarHibernate(String termo) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery(
					"SELECT DISTINCT f FROM Financeiro f "
						+ "LEFT JOIN f.cliente c "
						+ "LEFT JOIN f.fornecedor fo "
						+ "LEFT JOIN f.tipoConta tc "
						+ "LEFT JOIN f.formaPagamento fp "
						+ "WHERE lower(c.nome) LIKE :termo "
						+ "OR lower(fo.nome) LIKE :termo "
						+ "OR lower(tc.descricao) LIKE :termo "
						+ "OR lower(fp.nome) LIKE :termo "
						+ "ORDER BY f.id",
						Financeiro.class)
					.setParameter("termo", "%" + termo.toLowerCase() + "%")
					.list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public Financeiro pesquisarHibernate(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.find(Financeiro.class, id);
		} catch (Exception e) {
			return null;
		}
	}
}