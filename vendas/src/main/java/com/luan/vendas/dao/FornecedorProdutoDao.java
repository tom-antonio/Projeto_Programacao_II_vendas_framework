package com.luan.vendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.FornecedorProduto;

import jakarta.transaction.SystemException;

public class FornecedorProdutoDao {

    public boolean salvarHibernate(FornecedorProduto fornecedorProduto) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.persist(fornecedorProduto);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(FornecedorProduto fornecedorProduto) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.merge(fornecedorProduto);
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
			FornecedorProduto fornecedorProduto = session.find(FornecedorProduto.class, id);

			if (fornecedorProduto == null) {
				transaction.rollback();
				return false;
			}

			session.remove(fornecedorProduto);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao excluir fornecedor produto: " + e.getMessage());
			return false;
		}
	}

	public List<FornecedorProduto> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM FornecedorProduto", FornecedorProduto.class).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public FornecedorProduto pesquisarHibernate(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.find(FornecedorProduto.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	public FornecedorProduto buscarPorProdutoId(int produtoId) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			String hql = "FROM FornecedorProduto WHERE produto.id = :produtoId";
			List<FornecedorProduto> resultado = session.createQuery(hql, FornecedorProduto.class)
				.setParameter("produtoId", produtoId)
				.list();
			return resultado.isEmpty() ? null : resultado.get(0);
		} catch (Exception e) {
			return null;
		}
	}
}