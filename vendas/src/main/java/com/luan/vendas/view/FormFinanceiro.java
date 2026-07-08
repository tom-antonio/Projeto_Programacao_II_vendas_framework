package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.luan.vendas.controller.ClienteController;
import com.luan.vendas.controller.FinanceiroController;
import com.luan.vendas.controller.FinanceiroParcelaController;
import com.luan.vendas.controller.FormaPagamentoController;
import com.luan.vendas.controller.FornecedorController;
import com.luan.vendas.controller.TipoContaController;
import com.luan.vendas.model.Cliente;
import com.luan.vendas.model.Compra;
import com.luan.vendas.model.Financeiro;
import com.luan.vendas.model.FormaPagamento;
import com.luan.vendas.model.Fornecedor;
import com.luan.vendas.model.TipoConta;
import com.luan.vendas.model.Venda;

public class FormFinanceiro extends JFrame {

	private static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private JTextField txtDataConta;
	private JComboBox<String> comboPagarOuReceber;
	private JComboBox<Fornecedor> comboFornecedor;
	private JComboBox<Cliente> comboCliente;
	private JComboBox<TipoConta> comboTipoConta;
	private JComboBox<FormaPagamento> comboFormaPagamento;
	    private JComboBox<String> comboBoxparcelamento;
	private JButton btnAbrirFinanceiroParcela;
	private JButton btnSalvar;
	private JButton btnAlterar;
	private JButton btnExcluir;
	private JButton btnPesquisar;

	private final FinanceiroController financeiroController;
	private final FinanceiroParcelaController financeiroParcelaController;
	private final FornecedorController fornecedorController;
	private final ClienteController clienteController;
	private final TipoContaController tipoContaController;
	private final FormaPagamentoController formaPagamentoController;
	private final Window janelaPai;
	private Integer idFinanceiroAtual;

	public FormFinanceiro() throws ParseException {
		this(null, null, null);
	}

	public FormFinanceiro(Compra compraPreenchida) throws ParseException {
		this(null, compraPreenchida, null);
	}

	public FormFinanceiro(Venda vendaPreenchida) throws ParseException {
		this(null, null, vendaPreenchida);
	}

	public FormFinanceiro(Window janelaPai, Compra compraPreenchida) throws ParseException {
		this(janelaPai, compraPreenchida, null);
	}

	public FormFinanceiro(Window janelaPai, Venda vendaPreenchida) throws ParseException {
		this(janelaPai, null, vendaPreenchida);
	}

	private FormFinanceiro(Window janelaPai, Compra compraPreenchida, Venda vendaPreenchida) throws ParseException {
		setTitle("Cadastro de Financeiro");
		financeiroController = new FinanceiroController();
		financeiroParcelaController = new FinanceiroParcelaController();
		fornecedorController = new FornecedorController();
		clienteController = new ClienteController();
		tipoContaController = new TipoContaController();
		formaPagamentoController = new FormaPagamentoController();
		this.janelaPai = janelaPai;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setAutoRequestFocus(true);

		inicializarComponentes();
		carregarCombosRelacionamentos();
		if (compraPreenchida != null) {
			preencherCamposCompra(compraPreenchida);
		} else if (vendaPreenchida != null) {
			preencherCamposVenda(vendaPreenchida);
		}
		atualizarVisibilidadeFinanceiroParcela();
		configurarBloqueioDaJanelaPai();

		pack();
		setMinimumSize(new Dimension(700, 380));
		setLocationRelativeTo(null);
	}

	private void configurarBloqueioDaJanelaPai() {
		if (janelaPai == null) {
			return;
		}

		if (janelaPai instanceof JFrame framePai) {
			framePai.setEnabled(false);
		}

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {
				if (janelaPai instanceof JFrame framePai) {
					framePai.setEnabled(true);
					framePai.toFront();
					framePai.requestFocus();
				}
			}
		});
	}

	private void inicializarComponentes() throws ParseException {
		JPanel painelPrincipal = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		comboBoxparcelamento.addActionListener(e -> atualizarVisibilidadeFinanceiroParcela());
		painelPrincipal.add(new JLabel("Data da Conta:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;

        MaskFormatter mascaraData = new MaskFormatter("##/##/####");
        mascaraData.setPlaceholderCharacter('_');    
        txtDataConta = new JFormattedTextField(mascaraData);

        ((JFormattedTextField) txtDataConta).setColumns(28);
		painelPrincipal.add(txtDataConta, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		painelPrincipal.add(new JLabel("Pagar ou Receber:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		comboPagarOuReceber = new JComboBox<>(new String[] {"Pagar", "Receber"});
		painelPrincipal.add(comboPagarOuReceber, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		painelPrincipal.add(new JLabel("Fornecedor:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		comboFornecedor = new JComboBox<>();
		painelPrincipal.add(comboFornecedor, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		painelPrincipal.add(new JLabel("Cliente:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		comboCliente = new JComboBox<>();
		painelPrincipal.add(comboCliente, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		painelPrincipal.add(new JLabel("Tipo de Conta:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		comboTipoConta = new JComboBox<>();
		painelPrincipal.add(comboTipoConta, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		painelPrincipal.add(new JLabel("Forma de Pagamento:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		comboFormaPagamento = new JComboBox<>();
		painelPrincipal.add(comboFormaPagamento, gbc);

		gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Parcelamento:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
		comboBoxparcelamento = new JComboBox<>(new String[] {"Sim", "Não"});
		comboBoxparcelamento.addActionListener(e -> atualizarVisibilidadeFinanceiroParcela());
        painelPrincipal.add(comboBoxparcelamento, gbc);

		btnAbrirFinanceiroParcela = new JButton("Abrir Financeiro Parcela");
		btnAbrirFinanceiroParcela.addActionListener(e -> {
			try {
				FormFinanceiroParcela formFinanceiroParcela = new FormFinanceiroParcela(this);
				formFinanceiroParcela.toFront();
			} catch (ParseException ex) {
				JOptionPane.showMessageDialog(
					this,
					"Não foi possível abrir o financeiro parcela: " + ex.getMessage(),
					"Erro",
					JOptionPane.ERROR_MESSAGE
				);
			}
		});

		JPanel painelAbrirParcela = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		painelAbrirParcela.add(btnAbrirFinanceiroParcela);

		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		painelPrincipal.add(painelAbrirParcela, gbc);

		JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		btnSalvar = new JButton("Salvar");
		btnAlterar = new JButton("Alterar");
		btnExcluir = new JButton("Excluir");
		btnPesquisar = new JButton("Pesquisar");

		btnSalvar.addActionListener(e -> salvarFinanceiro());
		btnAlterar.addActionListener(e -> {
			if (precisaPesquisarFinanceiro()) {
				abrirPesquisaFinanceiro();
				return;
			}

			Financeiro financeiro = montarFinanceiroAtual();
			if (financeiro == null) {
				return;
			}

			boolean alterado = financeiroController.alterarFinanceiro(financeiro);
			if (!alterado) {
				JOptionPane.showMessageDialog(this, "Não foi possível alterar o financeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			JOptionPane.showMessageDialog(this, "Financeiro alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			limparCampos();
		});

		btnExcluir.addActionListener(e -> {
			if (precisaPesquisarFinanceiro()) {
				abrirPesquisaFinanceiro();
				return;
			}

			int confirmacao = JOptionPane.showConfirmDialog(
				this,
				"Tem certeza que deseja excluir este financeiro?",
				"Confirmar Exclusão",
				JOptionPane.YES_NO_OPTION
			);

			if (confirmacao != JOptionPane.YES_OPTION) {
				return;
			}

			boolean excluido = financeiroController.excluirFinanceiro(idFinanceiroAtual);
			if (!excluido) {
				JOptionPane.showMessageDialog(this, "Não foi possível excluir o financeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Financeiro excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				limparCampos();
			}
		});

		btnPesquisar.addActionListener(e -> abrirPesquisaFinanceiro());

		painelBotoes.add(btnSalvar);
		painelBotoes.add(btnAlterar);
		painelBotoes.add(btnExcluir);
		painelBotoes.add(btnPesquisar);

		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		painelPrincipal.add(painelBotoes, gbc);

		add(painelPrincipal, BorderLayout.CENTER);
	}

	private boolean precisaPesquisarFinanceiro() {
		return idFinanceiroAtual == null;
	}

	private void carregarCombosRelacionamentos() {
		configurarRenderizadores();
		carregarFornecedores();
		carregarClientes();
		carregarTiposConta();
		carregarFormasPagamento();
	}

	private void configurarRenderizadores() {
		comboFornecedor.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value == null) {
					setText("Selecione um Fornecedor");
				} else if (value instanceof Fornecedor fornecedor) {
					setText(fornecedor.getNome_fantasia());
				}
				return this;
			}
		});

		comboCliente.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value == null) {
					setText("Selecione um Cliente");
				} else if (value instanceof Cliente cliente) {
					setText(cliente.getNome());
				}
				return this;
			}
		});

		comboTipoConta.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value == null) {
					setText("Selecione um Tipo de Conta");
				} else if (value instanceof TipoConta tipoConta) {
					setText(tipoConta.getDescricao());
				}
				return this;
			}
		});

		comboFormaPagamento.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value == null) {
					setText("Selecione uma Forma de Pagamento");
				} else if (value instanceof FormaPagamento formaPagamento) {
					setText(formaPagamento.getNome());
				}
				return this;
			}
		});
	}

	private void carregarFornecedores() {
		comboFornecedor.removeAllItems();
		comboFornecedor.addItem(null);
		for (Fornecedor fornecedor : fornecedorController.listarFornecedores()) {
			comboFornecedor.addItem(fornecedor);
		}
	}

	private void carregarClientes() {
		comboCliente.removeAllItems();
		comboCliente.addItem(null);
		for (Cliente cliente : clienteController.listarClientes()) {
			comboCliente.addItem(cliente);
		}
	}

	private void carregarTiposConta() {
		comboTipoConta.removeAllItems();
		comboTipoConta.addItem(null);
		for (TipoConta tipoConta : tipoContaController.listarTiposConta()) {
			comboTipoConta.addItem(tipoConta);
		}
	}

	private void carregarFormasPagamento() {
		comboFormaPagamento.removeAllItems();
		comboFormaPagamento.addItem(null);
		for (FormaPagamento formaPagamento : formaPagamentoController.listarFormasPagamento()) {
			comboFormaPagamento.addItem(formaPagamento);
		}
	}

	private void atualizarVisibilidadeFinanceiroParcela() {
		boolean parcelado = "Sim".equals(comboBoxparcelamento.getSelectedItem());
		btnAbrirFinanceiroParcela.setEnabled(parcelado);
		btnAbrirFinanceiroParcela.setVisible(parcelado);
		btnAbrirFinanceiroParcela.setFocusable(parcelado);
		btnAbrirFinanceiroParcela.getParent().setVisible(parcelado);
	}

	private void abrirPesquisaFinanceiro() {
		PesquisaFinanceiro dialog = new PesquisaFinanceiro(this, financeiroController, financeiroParcelaController);
		dialog.setVisible(true);

		Financeiro selecionado = dialog.getFinanceiroSelecionado();
		if (selecionado != null) {
			preencherCampos(selecionado);
		}
	}

	private void preencherCampos(Financeiro financeiro) {
		idFinanceiroAtual = financeiro.getId();
		txtDataConta.setText(financeiro.getData_conta() != null ? financeiro.getData_conta().format(UI_DATE_FORMATTER) : "");
		comboPagarOuReceber.setSelectedIndex(financeiro.getPagar_ou_receber() == 1 ? 0 : 1);
		boolean ehParcelamento = financeiro.getFinanceiroParcelas() != null && !financeiro.getFinanceiroParcelas().isEmpty();
		comboBoxparcelamento.setSelectedItem(ehParcelamento ? "Sim" : "Não");
		atualizarVisibilidadeFinanceiroParcela();


		selecionarItemPorId(comboFornecedor, financeiro.getFornecedor() != null ? financeiro.getFornecedor().getId() : 0, Fornecedor::getId);
		selecionarItemPorId(comboCliente, financeiro.getCliente() != null ? financeiro.getCliente().getId() : 0, Cliente::getId);
		selecionarItemPorId(comboTipoConta, financeiro.getTipoConta() != null ? financeiro.getTipoConta().getId() : 0, TipoConta::getId);
		selecionarItemPorId(comboFormaPagamento, financeiro.getFormaPagamento() != null ? financeiro.getFormaPagamento().getId() : 0, FormaPagamento::getId);
	}

	private void preencherCamposCompra(Compra compra) {
		if (compra.getData_compra() != null) {
			txtDataConta.setText(compra.getData_compra().format(UI_DATE_FORMATTER));
		}

		comboPagarOuReceber.setSelectedIndex(0);

		if (compra.getFornecedor() != null) {
			selecionarItemPorId(comboFornecedor, compra.getFornecedor().getId(), Fornecedor::getId);
		}

		comboCliente.setSelectedIndex(0);
	}

	private void preencherCamposVenda(Venda venda) {
		if (venda.getData_venda() != null) {
			txtDataConta.setText(venda.getData_venda().format(UI_DATE_FORMATTER));
		}

		comboPagarOuReceber.setSelectedIndex(1);

		if (venda.getCliente() != null) {
			selecionarItemPorId(comboCliente, venda.getCliente().getId(), Cliente::getId);
		}

		comboFornecedor.setSelectedIndex(0);
	}

	private void salvarFinanceiro() {
		Financeiro financeiro = montarFinanceiroAtual();
		if (financeiro == null) {
			return;
		}

		boolean salvo = financeiroController.salvarFinanceiro(financeiro);
		if (!salvo) {
			JOptionPane.showMessageDialog(this, "Não foi possível salvar o financeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, "Financeiro salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		limparCampos();
	}

	private void limparCampos() {
		txtDataConta.setText("");
		comboPagarOuReceber.setSelectedIndex(0);
		comboFornecedor.setSelectedIndex(0);
		comboCliente.setSelectedIndex(0);
		comboTipoConta.setSelectedIndex(0);
		comboFormaPagamento.setSelectedIndex(0);
		idFinanceiroAtual = null;
		txtDataConta.requestFocus();
	}

	private Financeiro montarFinanceiroAtual() {
		LocalDate dataConta;
		try {
			dataConta = LocalDate.parse(txtDataConta.getText().trim(), UI_DATE_FORMATTER);
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(this, "Informe uma data válida no formato dd/MM/yyyy.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return null;
		}

		int pagarOuReceber = comboPagarOuReceber.getSelectedIndex() == 0 ? 1 : 0;
		Fornecedor fornecedor = (Fornecedor) comboFornecedor.getSelectedItem();
		Cliente cliente = (Cliente) comboCliente.getSelectedItem();
		TipoConta tipoConta = (TipoConta) comboTipoConta.getSelectedItem();
		FormaPagamento formaPagamento = (FormaPagamento) comboFormaPagamento.getSelectedItem();

		if (tipoConta == null || tipoConta.getId() <= 0) {
			JOptionPane.showMessageDialog(this, "Informe um Tipo de Conta válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return null;
		}

		if (formaPagamento == null || formaPagamento.getId() <= 0) {
			JOptionPane.showMessageDialog(this, "Informe uma Forma de Pagamento válida.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return null;
		}

		Financeiro financeiro = new Financeiro();
		int idFinanceiro = 0;
		if (idFinanceiroAtual != null) {
			idFinanceiro = idFinanceiroAtual;
		}
		financeiro.setId(idFinanceiro);
		financeiro.setData_conta(dataConta);
		financeiro.setPagar_ou_receber(pagarOuReceber);
		financeiro.setFornecedor(fornecedor);
		financeiro.setCliente(cliente);
		financeiro.setTipoConta(tipoConta);
		financeiro.setFormaPagamento(formaPagamento);
		financeiro.setFinanceiroParcelas(new ArrayList<>());
		return financeiro;
	}

	private <T> void selecionarItemPorId(JComboBox<T> comboBox, int id, java.util.function.ToIntFunction<T> idExtractor) {
		for (int i = 0; i < comboBox.getItemCount(); i++) {
			T item = comboBox.getItemAt(i);
			if (item != null && idExtractor.applyAsInt(item) == id) {
				comboBox.setSelectedIndex(i);
				return;
			}
		}
		comboBox.setSelectedIndex(0);
	}
}
