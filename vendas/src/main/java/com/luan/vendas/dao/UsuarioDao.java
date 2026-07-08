package com.luan.vendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.Usuario;

import jakarta.transaction.SystemException;

public class UsuarioDao {

	public boolean salvarHibernate(Usuario usuario) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = session.beginTransaction();
			session.persist(usuario);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(Usuario usuario) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = session.beginTransaction();
			session.merge(usuario);
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
			Usuario usuario = session.find(Usuario.class, id);

			if (usuario == null) {
				transaction.rollback();
				return false;
			}

			session.remove(usuario);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public List<Usuario> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM Usuario", Usuario.class).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public List<Usuario> pesquisarHibernate(String login) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery(
					"FROM Usuario u WHERE lower(u.login) LIKE :login ORDER BY u.login",
					Usuario.class)
					.setParameter("login", "%" + login.toLowerCase() + "%")
					.list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public Usuario pesquisarHibernate(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.find(Usuario.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	public Usuario buscarPorLoginESenha(String login, String senha) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery(
					"FROM Usuario u WHERE lower(u.login) = :login AND u.senha = :senha",
					Usuario.class)
					.setParameter("login", login.toLowerCase())
					.setParameter("senha", senha)
					.uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}

	public Usuario buscarPorLogin(String login) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery(
					"FROM Usuario u WHERE lower(u.login) = :login",
					Usuario.class)
					.setParameter("login", login.toLowerCase())
					.uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}
}
