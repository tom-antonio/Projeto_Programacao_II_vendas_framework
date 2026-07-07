package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.luan.vendas.controller.FinanceiroController;
import com.luan.vendas.controller.FinanceiroParcelaController;
import com.luan.vendas.model.Financeiro;
import com.luan.vendas.model.FinanceiroParcela;

public class PesquisaFinanceiro extends JDialog {

    private final JTextField txtPesquisa;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;
    private final FinanceiroController financeiroController;
    private final FinanceiroParcelaController financeiroParcelaController;
    private final List<Financeiro> financeirosEncontrados;
    private Financeiro financeiroSelecionado;

    public PesquisaFinanceiro(Frame parent, FinanceiroController financeiroController, FinanceiroParcelaController financeiroParcelaController) {
        super(parent, "Pesquisar Financeiro", true);
        this.financeiroController = financeiroController;
        this.financeiroParcelaController = financeiroParcelaController;
        this.financeirosEncontrados = new ArrayList<>();

        setSize(1100, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtPesquisa = new JTextField(30);
        JButton btnBuscar = new JButton("Buscar");
        painelTopo.add(new JLabel("Pesquisar por ID ou termo:"));
        painelTopo.add(txtPesquisa);
        painelTopo.add(btnBuscar);
        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[] { "ID", "Data", "Pagar/Receber", "Fornecedor", "Cliente", "Tipo Conta", "Forma Pagamento", "Parcelas" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnSelecionar = new JButton("Selecionar");
        JPanel painelBaixo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBaixo.add(btnSelecionar);
        add(painelBaixo, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarFinanceiros());
        txtPesquisa.addActionListener(e -> buscarFinanceiros());
        btnSelecionar.addActionListener(e -> selecionarFinanceiro());
    }

    private void buscarFinanceiros() {
        String textoPesquisa = txtPesquisa.getText().trim();

        if (textoPesquisa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um termo ou ID para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        financeirosEncontrados.clear();
        modeloTabela.setRowCount(0);

        try {
            int id = Integer.parseInt(textoPesquisa);
            Financeiro financeiro = financeiroController.pesquisarFinanceiro(id);
            if (financeiro != null) {
                financeirosEncontrados.add(financeiro);
            }
        } catch (NumberFormatException e) {
            financeirosEncontrados.addAll(financeiroController.listarFinanceirosPorTermo(textoPesquisa));
        }

        for (Financeiro financeiro : financeirosEncontrados) {
            List<FinanceiroParcela> parcelas = financeiroParcelaController.listarFinanceiroParcelasPorFinanceiro(financeiro.getId());
            modeloTabela.addRow(new Object[] {
                financeiro.getId(),
                financeiro.getData_conta(),
                financeiro.getPagar_ou_receber() == 1 ? "Pagar" : "Receber",
                financeiro.getFornecedor() != null ? financeiro.getFornecedor().getNome() : "",
                financeiro.getCliente() != null ? financeiro.getCliente().getNome() : "",
                financeiro.getTipoConta() != null ? financeiro.getTipoConta().getDescricao() : "",
                financeiro.getFormaPagamento() != null ? financeiro.getFormaPagamento().getNome() : "",
                parcelas.size()
            });
        }

        if (financeirosEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum financeiro encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionarFinanceiro() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um financeiro na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        financeiroSelecionado = financeirosEncontrados.get(linha);
        dispose();
    }

    public Financeiro getFinanceiroSelecionado() {
        return financeiroSelecionado;
    }
}