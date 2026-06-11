package com.luan.vendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.Fornecedor;

import jakarta.transaction.SystemException;

public class FornecedorDao {

    public boolean salvarHibernate(Fornecedor fornecedor) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.persist(fornecedor);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(Fornecedor fornecedor) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.merge(fornecedor);
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
			Fornecedor fornecedor = session.find(Fornecedor.class, id);

			if (fornecedor == null) {
				transaction.rollback();
				return false;
			}

			session.remove(fornecedor);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao excluir fornecedor: " + e.getMessage());
			return false;
		}
	}

	public List<Fornecedor> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM Fornecedor", Fornecedor.class).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<Fornecedor> pesquisarHibernate(String nome) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM Fornecedor f WHERE lower(f.nome) LIKE :nome order by f.nome", Fornecedor.class)
					.setParameter("nome", "%" + nome.toLowerCase() + "%")
					.list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public Fornecedor pesquisarHibernate(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.find(Fornecedor.class, id);
		} catch (Exception e) {
			return null;
		}
	}
}