package com.luan.vendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.FinanceiroParcela;

import jakarta.transaction.SystemException;

public class FinanceiroParcelaDao {

	public boolean salvarHibernate(FinanceiroParcela financeiroParcela) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.persist(financeiroParcela);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(FinanceiroParcela financeiroParcela) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.merge(financeiroParcela);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean excluirHibernate(FinanceiroParcela financeiroParcela) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			FinanceiroParcela parcelaEncontrada = session.find(FinanceiroParcela.class, financeiroParcela.getId());

			if (parcelaEncontrada == null) {
				transaction.rollback();
				return false;
			}

			session.remove(parcelaEncontrada);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao excluir financeiro parcela: " + e.getMessage());
			return false;
		}
	}

	public List<FinanceiroParcela> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM FinanceiroParcela", FinanceiroParcela.class).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<FinanceiroParcela> pesquisarHibernate(int financeiroId) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery(
					"FROM FinanceiroParcela fp WHERE fp.financeiro.id = :financeiroId ORDER BY fp.n_parcela",
					FinanceiroParcela.class)
					.setParameter("financeiroId", financeiroId)
					.list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public FinanceiroParcela pesquisarHibernatePorId(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.find(FinanceiroParcela.class, id);
		} catch (Exception e) {
			return null;
		}
	}
}