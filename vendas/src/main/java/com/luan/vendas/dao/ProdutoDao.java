package com.luan.vendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.luan.vendas.model.Produto;

import jakarta.transaction.SystemException;

public class ProdutoDao {

    public boolean salvarHibernate(Produto produto) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.persist(produto);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean alterarHibernate(Produto produto) throws IllegalStateException, SystemException {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = (Transaction) session.beginTransaction();
			session.merge(produto);
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
			Produto produto = session.find(Produto.class, id);

			if (produto == null) {
				transaction.rollback();
				return false;
			}

			session.remove(produto);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao excluir produto: " + e.getMessage());
			return false;
		}
	}

	public List<Produto> pesquisarHibernate() {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM Produto", Produto.class).list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public static List<Produto> pesquisarHibernate(String nome) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.createQuery("FROM Produto p WHERE lower(p.nome) LIKE :nome order by p.nome", Produto.class)
					.setParameter("nome", "%" + nome.toLowerCase() + "%")
					.list();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public Produto pesquisarHibernate(int id) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			return session.find(Produto.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	public double buscarPrecoMedio(int idProduto) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			Double precoMedio = session.createQuery(
					"SELECT avg(cp.valorUnit) FROM CompraProduto cp WHERE cp.produto.id = :idProduto",
					Double.class)
					.setParameter("idProduto", idProduto)
					.uniqueResult();

			return precoMedio != null ? precoMedio : 0.0;
		} catch (Exception e) {
			return 0.0;
		}
	}

	public double buscarValorUltimaCompra(int idProduto) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			Double valorUltimaCompra = session.createQuery(
					"SELECT cp.valorUnit FROM CompraProduto cp "
						+ "WHERE cp.produto.id = :idProduto "
						+ "ORDER BY cp.compra.data_compra DESC, cp.id DESC",
					Double.class)
					.setParameter("idProduto", idProduto)
					.setMaxResults(1)
					.uniqueResult();

			return valorUltimaCompra != null ? valorUltimaCompra : 0.0;
		} catch (Exception e) {
			return 0.0;
		}
	}

	public double buscarValorUltimaVenda(int idProduto) {
		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			Double valorUltimaVenda = session.createQuery(
					"SELECT pv.valorUnit FROM ProdutoVenda pv "
						+ "WHERE pv.produto.id = :idProduto "
						+ "ORDER BY pv.venda.data_venda DESC, pv.id DESC",
					Double.class)
					.setParameter("idProduto", idProduto)
					.setMaxResults(1)
					.uniqueResult();

			return valorUltimaVenda != null ? valorUltimaVenda : 0.0;
		} catch (Exception e) {
			return 0.0;
		}
	}

	public boolean atualizarEstoque(Produto produto) {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = session.beginTransaction();
			Produto produtoPersistido = session.find(Produto.class, produto.getId());

			if (produtoPersistido == null) {
				if (transaction != null) {
					transaction.rollback();
				}
				return false;
			}

			produtoPersistido.setQtde_estoque(produto.getQtde_estoque());
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao atualizar estoque do produto: " + e.getMessage());
			return false;
		}
	}

	public boolean atualizarValorUltimaCompra(Produto produto) {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = session.beginTransaction();
			Produto produtoPersistido = session.find(Produto.class, produto.getId());

			if (produtoPersistido == null) {
				if (transaction != null) {
					transaction.rollback();
				}
				return false;
			}

			produtoPersistido.setValor_ultima_compra(produto.getValor_ultima_compra());
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao atualizar valor_ultima_compra do produto: " + e.getMessage());
			return false;
		}
	}

	public boolean atualizarPrecoMedio(Produto produto) {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = session.beginTransaction();
			Double precoMedio = session.createQuery(
					"SELECT avg(cp.valorUnit) FROM CompraProduto cp WHERE cp.produto.id = :idProduto", Double.class)
					.setParameter("idProduto", produto.getId())
					.uniqueResult();

			Produto produtoPersistido = session.find(Produto.class, produto.getId());

			if (produtoPersistido == null) {
				if (transaction != null) {
					transaction.rollback();
				}
				return false;
			}

			produtoPersistido.setPreco_medio(precoMedio != null ? precoMedio : 0.0);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao atualizar preco_medio do produto: " + e.getMessage());
			return false;
		}
	}

	public boolean atualizarValorUltimaVenda(Produto produto) {
		Transaction transaction = null;

		try (Session session = Postgres.getSESSION_FACTORY().openSession()) {
			transaction = session.beginTransaction();
			Produto produtoPersistido = session.find(Produto.class, produto.getId());

			if (produtoPersistido == null) {
				if (transaction != null) {
					transaction.rollback();
				}
				return false;
			}

			produtoPersistido.setValor_ultima_venda(produto.getValor_ultima_venda());
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			System.out.println("Erro ao atualizar valor_ultima_venda do produto: " + e.getMessage());
			return false;
		}
	}
}