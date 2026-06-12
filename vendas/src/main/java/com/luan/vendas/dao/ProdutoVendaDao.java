package com.luan.vendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.ProdutoVenda;

import jakarta.transaction.SystemException;

public class ProdutoVendaDao {

    public boolean salvarHibernate(ProdutoVenda produtoVenda) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = session.beginTransaction();
			session.persist(produtoVenda);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(ProdutoVenda produtoVenda) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = session.beginTransaction();
			session.merge(produtoVenda);
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
			ProdutoVenda produtoVenda = session.find(ProdutoVenda.class, id);

			if (produtoVenda == null) {
				transaction.rollback();
				return false;
			}

			session.remove(produtoVenda);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao excluir relação produto-venda: " + e.getMessage());
			return false;
		}
	}

	public List<ProdutoVenda> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM ProdutoVenda", ProdutoVenda.class).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<ProdutoVenda> pesquisarHibernate(int vendaId) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery(
					"FROM ProdutoVenda pv WHERE pv.venda.id = :vendaId ORDER BY pv.produto.id",
					ProdutoVenda.class)
					.setParameter("vendaId", vendaId)
					.list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public ProdutoVenda pesquisarHibernatePorId(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.find(ProdutoVenda.class, id);
		} catch (Exception e) {
			return null;
		}
	}
}