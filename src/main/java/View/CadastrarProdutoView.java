package View;

import Controller.ProdutoController;
import Model.Produto;

import javax.swing.*;
import java.awt.*;

public class CadastrarProdutoView extends JFrame {
    private final ProdutoController controller = new ProdutoController();
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtQuantidade;
    private JTextField txtPreco;
    private JButton btnSalvar;
    private boolean modoEdicao = false;

    public CadastrarProdutoView() {
        setTitle("Cadastrar Produto");
        setSize(380, 260);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    public CadastrarProdutoView(Produto produto) {
        this();
        modoEdicao = true;
        setTitle("Editar Produto");
        preencherCampos(produto);
        btnSalvar.setText("Salvar Alterações");
    }

    private void initComponents() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        txtId = new JTextField(15);
        txtId.setEnabled(false);
        txtId.setToolTipText("Gerado automaticamente");
        txtNome = new JTextField(15);
        txtQuantidade = new JTextField(15);
        txtPreco = new JTextField(15);
        btnSalvar = new JButton("Cadastrar");
        btnSalvar.setPreferredSize(new Dimension(160, 32));

        addRow(painel, gbc, 0, "ID:", txtId);
        addRow(painel, gbc, 1, "Nome:", txtNome);
        addRow(painel, gbc, 2, "Quantidade:", txtQuantidade);
        addRow(painel, gbc, 3, "Preço (R$):", txtPreco);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(14, 6, 6, 6);
        painel.add(btnSalvar, gbc);

        btnSalvar.addActionListener(e -> salvar());
        add(painel);
    }

    private void addRow(JPanel painel, GridBagConstraints gbc, int row, String label, JComponent campo) {
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        gbc.gridx = 0; gbc.gridy = row;
        painel.add(new JLabel(label), gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.gridx = 1;
        painel.add(campo, gbc);
    }

    private void preencherCampos(Produto produto) {
        txtId.setText(String.valueOf(produto.getId()));
        txtNome.setText(produto.getNome());
        txtQuantidade.setText(String.valueOf(produto.getQuantidade()));
        txtPreco.setText(String.format("%.2f", produto.getPreco()));
    }

    private void salvar() {
        try {
            String nome = txtNome.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome do produto é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String qtdTexto = txtQuantidade.getText().trim();
            String precoTexto = txtPreco.getText().trim().replace(",", ".");
            if (qtdTexto.isEmpty() || precoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantidade = Integer.parseInt(qtdTexto);
            double preco = Double.parseDouble(precoTexto);
            if (quantidade < 0 || preco < 0) {
                JOptionPane.showMessageDialog(this, "Quantidade e preço não podem ser negativos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setQuantidade(quantidade);
            produto.setPreco(preco);

            if (modoEdicao) {
                produto.setId(Integer.parseInt(txtId.getText().trim()));
            }

            controller.salvar(produto);
            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores inválidos. Verifique quantidade e preço.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
