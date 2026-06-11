package com.luan.vendas.dao;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.Cliente;
import com.luan.vendas.model.Venda;

import jakarta.transaction.SystemException;

public class VendaDao {

    public boolean salvarHibernate(Venda venda) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.persist(venda);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(Venda venda) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.merge(venda);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean excluirHibernate(int id) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			Venda venda = session.find(Venda.class, id);

			if (venda == null) {
				transaction.rollback();
				return false;
			}

			session.remove(venda);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao excluir venda: " + e.getMessage());
			return false;
		}
	}

	public List<Venda> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM Venda", Venda.class).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<Venda> pesquisarHibernate(String nome) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM Venda v WHERE lower(v.nome) LIKE :nome order by v.nome", Venda.class)
					.setParameter("nome", "%" + nome.toLowerCase() + "%")
					.list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public Venda pesquisarHibernate(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.find(Venda.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	// Método para contar o número de vendas realizadas por um cliente nos últimos 30 dias
	public int contarVendas(Cliente cliente, Date dataVenda) {
		if (cliente == null || cliente.getId() <= 0 || dataVenda == null) {
			return 1000;
		}

		LocalDate dataFinalLocal = dataVenda.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dataInicialLocal = dataFinalLocal.minusDays(30);

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			Long total = session.createQuery(
							"SELECT count(v) FROM Venda v "
									+ "WHERE v.cliente.id = :idCliente "
									+ "AND v.data_venda >= :dataInicial "
									+ "AND v.data_venda < :dataFinal",
							Long.class)
					.setParameter("idCliente", cliente.getId())
					.setParameter("dataInicial", dataInicialLocal)
					.setParameter("dataFinal", dataFinalLocal)
					.uniqueResult();

			return total != null ? total.intValue() : 0;
		} catch (Exception e) {
			System.out.println("Erro ao contar vendas por cliente nos últimos 30 dias: " + e.getMessage());
			return 1000;
		}
	}
}