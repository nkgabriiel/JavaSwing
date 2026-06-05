package View;

import Controller.ProdutoController;
import Model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class ProdutoView extends JFrame {
    private final ProdutoController controller = new ProdutoController();
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField txtPesquisa;
    private List<Produto> produtosAtuais = new ArrayList<>();

    public ProdutoView() {
        setTitle("Consultar Estoque");
        setSize(680, 460);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        carregarProdutos(controller.listar());
    }

    private void initComponents() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search panel
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        painelPesquisa.setBorder(BorderFactory.createTitledBorder("Pesquisa"));
        txtPesquisa = new JTextField(22);
        JButton btnPesquisar = new JButton("Pesquisar");
        JButton btnLimpar = new JButton("Limpar");
        painelPesquisa.add(new JLabel("Nome:"));
        painelPesquisa.add(txtPesquisa);
        painelPesquisa.add(btnPesquisar);
        painelPesquisa.add(btnLimpar);

        btnPesquisar.addActionListener(e -> pesquisar());
        txtPesquisa.addActionListener(e -> pesquisar());
        btnLimpar.addActionListener(e -> {
            txtPesquisa.setText("");
            carregarProdutos(controller.listar());
        });

        // Table
        String[] colunas = {"ID", "Nome", "Quantidade", "Preço (R$)"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(22);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.getColumnModel().getColumn(0).setMaxWidth(55);
        tabela.getColumnModel().getColumn(2).setMaxWidth(100);
        tabela.getColumnModel().getColumn(3).setMaxWidth(110);
        JScrollPane scrollPane = new JScrollPane(tabela);

        // Button panel
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar");

        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregarProdutos(controller.listar()));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        painel.add(painelPesquisa, BorderLayout.NORTH);
        painel.add(scrollPane, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        add(painel);
    }

    private void carregarProdutos(List<Produto> produtos) {
        produtosAtuais = produtos;
        modeloTabela.setRowCount(0);
        for (Produto p : produtos) {
            modeloTabela.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getQuantidade(),
                    String.format("%.2f", p.getPreco())
            });
        }
    }

    private void pesquisar() {
        String termo = txtPesquisa.getText().trim();
        if (termo.isEmpty()) {
            carregarProdutos(controller.listar());
        } else {
            carregarProdutos(controller.buscarPorNome(termo));
        }
    }

    private Produto getProdutoSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return null;
        return produtosAtuais.get(linha);
    }

    private void editar() {
        Produto produto = getProdutoSelecionado();
        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        CadastrarProdutoView tela = new CadastrarProdutoView(produto);
        tela.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                carregarProdutos(controller.listar());
            }
        });
        tela.setVisible(true);
    }

    private void excluir() {
        Produto produto = getProdutoSelecionado();
        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir o produto \"" + produto.getNome() + "\"?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirmacao == JOptionPane.YES_OPTION) {
            controller.remover(produto.getId());
            carregarProdutos(controller.listar());
            JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
