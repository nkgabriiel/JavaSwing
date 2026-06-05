package View;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    public MenuPrincipalView() {
        setTitle("Papelaria - Menu Principal");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Title area
        JPanel painelTitulo = new JPanel(new GridLayout(3, 1, 4, 4));
        JLabel lblTitulo = new JLabel("Sistema de Controle de Estoque", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 17));
        JLabel lblSubtitulo = new JLabel("Papelaria", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 13));
        lblSubtitulo.setForeground(Color.DARK_GRAY);
        JSeparator sep = new JSeparator();
        painelTitulo.add(lblTitulo);
        painelTitulo.add(lblSubtitulo);
        painelTitulo.add(sep);

        // Buttons area
        JButton btnConsultar = new JButton("Consultar Estoque");
        JButton btnNovoProduto = new JButton("Novo Produto");

        Font fonteBotao = new Font("Arial", Font.PLAIN, 14);
        btnConsultar.setFont(fonteBotao);
        btnNovoProduto.setFont(fonteBotao);

        btnConsultar.addActionListener(e -> new ProdutoView().setVisible(true));
        btnNovoProduto.addActionListener(e -> new CadastrarProdutoView().setVisible(true));

        JPanel painelBotoes = new JPanel(new GridLayout(2, 1, 10, 12));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20));
        painelBotoes.add(btnConsultar);
        painelBotoes.add(btnNovoProduto);

        painelPrincipal.add(painelTitulo, BorderLayout.NORTH);
        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);

        add(painelPrincipal);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipalView().setVisible(true));
    }
}
