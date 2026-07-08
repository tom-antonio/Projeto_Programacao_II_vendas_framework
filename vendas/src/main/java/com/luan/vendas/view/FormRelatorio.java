package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import org.hibernate.Session;

import com.luan.vendas.controller.ClienteController;
import com.luan.vendas.controller.FornecedorController;
import com.luan.vendas.dao.Postgres;
import com.luan.vendas.model.Cliente;
import com.luan.vendas.model.Fornecedor;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class FormRelatorio extends JFrame {

    private final JRadioButton rbVenda;
    private final JRadioButton rbCompra;
    private final JRadioButton rbFinanceiro;

    private final JTextField txtDataInicial;
    private final JTextField txtDataFinal;

    private final JComboBox<Cliente> cbCliente;
    private final JComboBox<Fornecedor> cbFornecedor;
    private final JComboBox<String> cbPagarOuReceber;

    private final JButton btnGerar;

    public FormRelatorio() {

        setTitle("Relatórios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(new JLabel("Tipo:"), gbc);

        rbVenda = new JRadioButton("Vendas");
        rbCompra = new JRadioButton("Compras");
        rbFinanceiro = new JRadioButton("Financeiro");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbVenda);
        grupo.add(rbCompra);
        grupo.add(rbFinanceiro);

        rbVenda.setSelected(true);

        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painelTipo.add(rbVenda);
        painelTipo.add(rbCompra);
        painelTipo.add(rbFinanceiro);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        painel.add(painelTipo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painel.add(new JLabel("Data Inicial:"), gbc);

        MaskFormatter mascaraDataInicial;
        MaskFormatter mascaraDataFinal;
        try {
            mascaraDataInicial = new MaskFormatter("##/##/####");
            mascaraDataInicial.setPlaceholderCharacter('_');
            mascaraDataFinal = new MaskFormatter("##/##/####");
            mascaraDataFinal.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            throw new IllegalStateException("Não foi possível configurar a máscara de data.", e);
        }

        txtDataInicial = new JFormattedTextField(mascaraDataInicial);
        ((JFormattedTextField) txtDataInicial).setColumns(28);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        painel.add(txtDataInicial, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painel.add(new JLabel("Data Final:"), gbc);

        txtDataFinal = new JFormattedTextField(mascaraDataFinal);
        ((JFormattedTextField) txtDataFinal).setColumns(28);
        txtDataFinal.setToolTipText("dd/MM/yyyy");

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        painel.add(txtDataFinal, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painel.add(new JLabel("Cliente:"), gbc);

        cbCliente = new JComboBox<>();

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        painel.add(cbCliente, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painel.add(new JLabel("Fornecedor:"), gbc);

        cbFornecedor = new JComboBox<>();

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        painel.add(cbFornecedor, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painel.add(new JLabel("Pagar ou Receber:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        cbPagarOuReceber = new JComboBox<>();
        cbPagarOuReceber.addItem("Pagar");
        cbPagarOuReceber.addItem("Receber");

        painel.add(cbPagarOuReceber, gbc);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnGerar = new JButton("Gerar Relatório");

        botoes.add(btnGerar);

        add(painel, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        configurarRenderizadores();
        carregarClientes();
        carregarFornecedores();
        atualizarCampos();

        rbVenda.addActionListener(e -> atualizarCampos());
        rbCompra.addActionListener(e -> atualizarCampos());
        rbFinanceiro.addActionListener(e -> atualizarCampos());

        btnGerar.addActionListener(e -> gerarRelatorio());

        pack();
        setMinimumSize(new Dimension(720, 300));
        setLocationRelativeTo(null);
    }

    private void configurarRenderizadores() {
        cbCliente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("Todos os Clientes");
                } else if (value instanceof Cliente cliente) {
                    setText(cliente.getNome());
                }
                return this;
            }
        });

        cbFornecedor.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("Todos os Fornecedores");
                } else if (value instanceof Fornecedor fornecedor) {
                    setText(fornecedor.getNome_fantasia());
                }
                return this;
            }
        });
    }

    private void atualizarCampos() {
        cbCliente.setEnabled(rbVenda.isSelected());
        cbFornecedor.setEnabled(rbCompra.isSelected());
        cbPagarOuReceber.setEnabled(rbFinanceiro.isSelected());
    }

    private void carregarClientes() {

        ClienteController controller = new ClienteController();

        cbCliente.removeAllItems();
        cbCliente.addItem(null);

        try {
            for (Cliente c : controller.listarClientes()) {
                cbCliente.addItem(c);
            }
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível carregar os clientes para o filtro.\n\n" + e.getMessage(),
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void carregarFornecedores() {

        FornecedorController controller = new FornecedorController();

        cbFornecedor.removeAllItems();
        cbFornecedor.addItem(null);

        try {
            for (Fornecedor f : controller.listarFornecedores()) {
                cbFornecedor.addItem(f);
            }
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível carregar os fornecedores para o filtro.\n\n" + e.getMessage(),
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void gerarRelatorio() {

        String arquivo;

        if (rbVenda.isSelected()) {
            arquivo = "/relatorios/VendaRelatorio.jrxml";
        } else if (rbCompra.isSelected()) {
            arquivo = "/relatorios/CompraRelatorio.jrxml";
        } else {
            arquivo = "/relatorios/FinanceiroRelatorio.jrxml";
        }

        abrirRelatorio(arquivo);
    }

    private void abrirRelatorio(String arquivo) {

        Session session = null;

        try {

            if (txtDataInicial.getText().trim().isEmpty()
                    || txtDataFinal.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Informe a Data Inicial e a Data Final.\nFormato: dd/MM/yyyy");

                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate dataInicial = LocalDate.parse(txtDataInicial.getText(), formatter);

            LocalDate dataFinal = LocalDate.parse(txtDataFinal.getText(), formatter);

            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream(arquivo));

            session = Postgres.getSESSION_FACTORY().openSession();

            Connection conexao = session.doReturningWork(conn -> conn);

            HashMap<String, Object> parametros = new HashMap<>();

            parametros.put(
                    "dataInicial",
                    java.sql.Date.valueOf(dataInicial));

            parametros.put(
                    "dataFinal",
                    java.sql.Date.valueOf(dataFinal));

            if (rbFinanceiro.isSelected()) {
                parametros.put(
                        "pagarOuReceber",
                        cbPagarOuReceber.getSelectedIndex());
            }

            if (rbVenda.isSelected() && cbCliente.getSelectedItem() != null) {

                Cliente cliente = (Cliente) cbCliente.getSelectedItem();

                parametros.put("clienteId", cliente.getId());

            }

            if (rbCompra.isSelected() && cbFornecedor.getSelectedItem() != null) {

                Fornecedor fornecedor = (Fornecedor) cbFornecedor.getSelectedItem();

                parametros.put("fornecedorId", fornecedor.getId());

            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parametros,
                    conexao);

            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException | RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar relatório.\n\n"
                            + e.getMessage());

        } finally {

            if (session != null) {
                session.close();
            }

        }
    }

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(() -> {
            new FormRelatorio().setVisible(true);
        });

    }

}