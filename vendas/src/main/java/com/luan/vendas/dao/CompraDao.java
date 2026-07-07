package com.luan.vendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.Compra;

import jakarta.transaction.SystemException;

public class CompraDao {

    public boolean salvarHibernate(Compra compra) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.persist(compra);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(Compra compra) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.merge(compra);
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
			Compra compra = session.find(Compra.class, id);

			if (compra == null) {
				transaction.rollback();
				return false;
			}

			session.remove(compra);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao excluir compra: " + e.getMessage());
			return false;
		}
	}

	public List<Compra> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery(
				"select distinct c from Compra c left join fetch c.fornecedor left join fetch c.compraProduto cp left join fetch cp.produto order by c.id",
				Compra.class
			).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

    public List<Compra> pesquisarHibernate(String nome) {
		List<Compra> compras = pesquisarHibernate();
		if (nome == null || nome.trim().isEmpty()) {
			return compras;
		}

		String textoBusca = nome.trim().toLowerCase();
		return compras.stream()
			.filter(compra -> {
				String fornecedorTexto = compra.getFornecedor() != null ? compra.getFornecedor().getNome_fantasia() : "";
				return fornecedorTexto.toLowerCase().contains(textoBusca);
			})
			.toList();
		}

	public Compra pesquisarHibernate(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery(
				"select distinct c from Compra c left join fetch c.fornecedor left join fetch c.compraProduto cp left join fetch cp.produto where c.id = :id",
				Compra.class
			).setParameter("id", id).uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}
}