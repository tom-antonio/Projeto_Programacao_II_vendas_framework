package com.luan.vendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.FormaPagamento;

import jakarta.transaction.SystemException;

public class FormaPagamentoDao {

    public boolean salvarHibernate(FormaPagamento formaPagamento) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.persist(formaPagamento);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(FormaPagamento formaPagamento) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.merge(formaPagamento);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean excluirHibernate(FormaPagamento formaPagamento) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			FormaPagamento forma = session.find(FormaPagamento.class, formaPagamento.getId());

			if (forma == null) {
				transaction.rollback();
				return false;
			}

			session.remove(forma);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao excluir forma de pagamento: " + e.getMessage());
			return false;
		}
	}

	public List<FormaPagamento> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM FormaPagamento", FormaPagamento.class).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<FormaPagamento> pesquisarHibernate(String nome) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM FormaPagamento f WHERE lower(f.nome) LIKE :nome order by f.nome", FormaPagamento.class)
					.setParameter("nome", "%" + nome.toLowerCase() + "%")
					.list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public FormaPagamento pesquisarHibernate(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.find(FormaPagamento.class, id);
		} catch (Exception e) {
			return null;
		}
	}
}