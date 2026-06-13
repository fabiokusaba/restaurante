package dev.fabiokusaba.restaurante.service;

import dev.fabiokusaba.restaurante.domain.entities.CategoriaProduto;
import dev.fabiokusaba.restaurante.domain.entities.Produto;
import dev.fabiokusaba.restaurante.dto.ProdutoRequest;
import dev.fabiokusaba.restaurante.dto.ProdutoResponse;
import dev.fabiokusaba.restaurante.repository.CategoriaProdutoRepository;
import dev.fabiokusaba.restaurante.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final CategoriaProdutoRepository categoriaRepository;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            CategoriaProdutoRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public ProdutoResponse cadastrar(ProdutoRequest produtoRequest) {
        CategoriaProduto categoria = buscarCategoriaPorId(produtoRequest.categoriaId());
        Produto produto = produtoRequest.toEntity(categoria);
        Produto produtoSalvo = produtoRepository.save(produto);
        return ProdutoResponse.fromEntity(produtoSalvo);
    }

    public Page<ProdutoResponse> listar(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(ProdutoResponse::fromEntity);
    }

    public ProdutoResponse buscarPorId(Long id) {
        Produto produto = buscarProdutoPorId(id);
        return ProdutoResponse.fromEntity(produto);
    }

    public ProdutoResponse atualizar(Long id, ProdutoRequest produtoRequest) {
        Produto produto = buscarProdutoPorId(id);
        CategoriaProduto categoria = buscarCategoriaPorId(produtoRequest.categoriaId());
        produtoRequest.preencher(produto, categoria);
        Produto produtoAtualizado = produtoRepository.save(produto);
        return ProdutoResponse.fromEntity(produtoAtualizado);
    }

    public void excluir(Long id) {
        Produto produto = buscarProdutoPorId(id);
        produtoRepository.delete(produto);
    }

    private Produto buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    private CategoriaProduto buscarCategoriaPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }
}
