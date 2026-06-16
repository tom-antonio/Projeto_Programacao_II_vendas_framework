package com.luan.vendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.TipoConta;

import jakarta.transaction.SystemException;

public class TipoContaDao {

    public boolean salvarHibernate(TipoConta tipoConta) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.persist(tipoConta);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(TipoConta tipoConta) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.merge(tipoConta);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean excluirHibernate(TipoConta tipoConta) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			TipoConta tipoContaEncontrada = session.find(TipoConta.class, tipoConta.getId());

			if (tipoContaEncontrada == null) {
				transaction.rollback();
				return false;
			}

			session.remove(tipoContaEncontrada);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao excluir tipo de conta: " + e.getMessage());
			return false;
		}
	}

	public List<TipoConta> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM TipoConta", TipoConta.class).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<TipoConta> pesquisarHibernate(String nome) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM TipoConta t WHERE lower(t.descricao) LIKE :descricao order by t.descricao", TipoConta.class)
					.setParameter("descricao", "%" + nome.toLowerCase() + "%")
					.list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public TipoConta pesquisarHibernate(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.find(TipoConta.class, id);
		} catch (Exception e) {
			return null;
		}
	}
}