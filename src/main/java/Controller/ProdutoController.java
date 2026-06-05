package Controller;

import DAO.ProdutoDAO;
import Model.Produto;

import java.util.List;

public class ProdutoController {
    private final ProdutoDAO dao = new ProdutoDAO();

    public void salvar(Produto produto) {
        dao.salvar(produto);
    }

    public List<Produto> listar() {
        return dao.listar();
    }

    public List<Produto> buscarPorNome(String nome) {
        return dao.buscarPorNome(nome);
    }

    public void remover(int id) {
        dao.remover(id);
    }
}
