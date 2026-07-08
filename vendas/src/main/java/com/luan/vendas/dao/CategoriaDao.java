package com.luan.vendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.Categoria;

import jakarta.transaction.SystemException;

public class CategoriaDao {

	private void sincronizarEstruturaCategoria(Session session) {
		try {
			session.createNativeMutationQuery("alter table if exists categoria drop column if exists descricao").executeUpdate();
		} catch (Exception e) {
			System.out.println("Aviso: nao foi possivel sincronizar a estrutura da tabela categoria: " + e.getMessage());
		}
	}

    public boolean salvarHibernate(Categoria categoria) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			sincronizarEstruturaCategoria(session);
			session.persist(categoria);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(Categoria categoria) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			sincronizarEstruturaCategoria(session);
			session.merge(categoria);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean excluirHibernate(Categoria categoria2) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			Categoria categoria = session.find(Categoria.class, categoria2);

			if (categoria == null) {
				transaction.rollback();
				return false;
			}

			session.remove(categoria);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao excluir categoria: " + e.getMessage());
			return false;
		}
	}

	public List<Categoria> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM Categoria", Categoria.class).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<Categoria> pesquisarHibernate(String nome) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			String nomeBusca = nome == null ? "" : nome.trim().toLowerCase();
			return session.createQuery("FROM Categoria c WHERE lower(c.nome) LIKE :nome order by c.nome", Categoria.class)
					.setParameter("nome", "%" + nomeBusca + "%")
					.list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public Categoria pesquisarHibernate(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.find(Categoria.class, id);
		} catch (Exception e) {
			return null;
		}
	}
}