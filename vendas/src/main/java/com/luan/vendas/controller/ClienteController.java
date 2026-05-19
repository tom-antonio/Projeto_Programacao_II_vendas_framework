package com.luan.vendas.controller;
import com.luan.vendas.dao.ClienteDao;
import com.luan.vendas.model.Cliente;

public class ClienteController {

    private final ClienteDao clienteDao;

    public ClienteController() {
        this.clienteDao = new ClienteDao();
    }
    
    public boolean salvarCliente(int id, String nome, String cpf, String rg, String endereco,String telefone) {
        if (id <= 0) {
            return false;
        }
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        if (rg == null || rg.trim().isEmpty()) {
            return false;
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            return false;
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }

        Cliente cliente = new Cliente();
    cliente.setId(id);
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setRg(rg);
        cliente.setEndereco(endereco);
        cliente.setTelefone(telefone);

        return clienteDao.salvar(cliente);
    }

    public boolean alterarCliente(int id, String nome, String cpf, String rg, String endereco,String telefone) {
        if (id <= 0) {
            return false;
        }
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        if (rg == null || rg.trim().isEmpty()) {
            return false;
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            return false;
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setRg(rg);
        cliente.setEndereco(endereco);
        cliente.setTelefone(telefone);

        return clienteDao.alterar(cliente);
    }

    public boolean excluirCliente(int id) {
        if (id <= 0) {
            return false;
        }

        return clienteDao.excluir(id);
    }

    public Cliente pesquisarCliente(int id) {
        if (id <= 0) {
            return null;
        }

        return clienteDao.pesquisar(id);
    }
    
}
