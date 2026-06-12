package com.luan.vendas;

import javax.swing.SwingUtilities;

import com.luan.vendas.view.FormTelaPrincipal;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormTelaPrincipal());
    }

}