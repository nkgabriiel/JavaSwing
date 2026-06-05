package DAO;

import Model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public ProdutoDAO() {
        criarTabela();
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS produto (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "nome VARCHAR(255) NOT NULL," +
                "quantidade INT NOT NULL," +
                "preco DOUBLE NOT NULL)";
        try (Connection conn = ConexaoDAO.getConexao();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela: " + e.getMessage(), e);
        }
    }

    public void salvar(Produto produto) {
        if (produto.getId() == 0) {
            String sql = "INSERT INTO produto (nome, quantidade, preco) VALUES (?, ?, ?)";
            try (Connection conn = ConexaoDAO.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, produto.getNome());
                ps.setInt(2, produto.getQuantidade());
                ps.setDouble(3, produto.getPreco());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao inserir produto: " + e.getMessage(), e);
            }
        } else {
            String sql = "UPDATE produto SET nome=?, quantidade=?, preco=? WHERE id=?";
            try (Connection conn = ConexaoDAO.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, produto.getNome());
                ps.setInt(2, produto.getQuantidade());
                ps.setDouble(3, produto.getPreco());
                ps.setInt(4, produto.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
            }
        }
    }

    public List<Produto> listar() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto ORDER BY nome";
        try (Connection conn = ConexaoDAO.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                produtos.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }
        return produtos;
    }

    public List<Produto> buscarPorNome(String nome) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE nome LIKE ? ORDER BY nome";
        try (Connection conn = ConexaoDAO.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                produtos.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos: " + e.getMessage(), e);
        }
        return produtos;
    }

    public void remover(int id) {
        String sql = "DELETE FROM produto WHERE id=?";
        try (Connection conn = ConexaoDAO.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover produto: " + e.getMessage(), e);
        }
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getInt("quantidade"),
                rs.getDouble("preco")
        );
    }
}
