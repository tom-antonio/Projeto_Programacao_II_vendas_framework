package com.luan.vendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.CompraProduto;

import jakarta.transaction.SystemException;

public class CompraProdutoDao {

    public boolean salvarHibernate(CompraProduto compraProduto) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = session.beginTransaction();
			session.persist(compraProduto);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(CompraProduto compraProduto) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = session.beginTransaction();
			session.merge(compraProduto);
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
			transaction = session.beginTransaction();
			CompraProduto compraProduto = session.find(CompraProduto.class, id);

			if (compraProduto == null) {
				transaction.rollback();
				return false;
			}

			session.remove(compraProduto);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao excluir relação compra-produto: " + e.getMessage());
			return false;
		}
	}

	public List<CompraProduto> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM CompraProduto", CompraProduto.class).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<CompraProduto> pesquisarHibernate(int compraId) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM CompraProduto cp WHERE cp.compra.id = :compraId ORDER BY cp.produto.id", CompraProduto.class)
					.setParameter("compraId", compraId)
					.list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public CompraProduto pesquisarHibernatePorId(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.find(CompraProduto.class, id);
		} catch (Exception e) {
			return null;
		}
	}
}