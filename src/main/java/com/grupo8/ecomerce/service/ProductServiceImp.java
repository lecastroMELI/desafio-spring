package com.grupo8.ecomerce.service;

import com.grupo8.ecomerce.dto.OrderRequestDto;
import com.grupo8.ecomerce.model.Product;
import com.grupo8.ecomerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {

    /**
     * Injeção de dependência Repository: Product
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Método responsável por criar um novo produto.
     * @param newProduct Dados do novo produto.
     */
    @Override
    public void createProduct(Product newProduct) {
        productRepository.createProduct(newProduct);
    }

    /**
     * Método responsável por retornar uma lista de todos os produtos cadastros.
     * @return Lista de. produtos
     */
    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = productRepository.getAllProducts();
        return productList;
    }

    /**
     * Método responsável por retornar uma lista de produtos de acordo com uma categoria específica.
     * @param category A categoria a ser utilizada na pesquisa.
     * @return Lista de produtos que pertencem à categoria informada.
     */
    @Override
    public List<Product> getByCategory(String category) {
        List<Product> productList = productRepository.getAllProducts();
        List<Product> productByCategoryList = productList.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        return productByCategoryList;
    }

    /**
     * Método responsável por retornar uma lista de produtos que possuem frete grátis.
     * @param productList Lista contendo todos os produtos.
     * @return Lista de produtos com frete grátis.
     */
    @Override
    public List<Product> getByShipping(List<Product> productList) { // category avaliacao
        List<Product> productsFreeShipping = productList.stream()
                .filter(product -> product.getFreeShipping() == true)
                .collect(Collectors.toList());
        return productsFreeShipping;
    }

    /**
     * Método responsável por retornar uma lista de produtos que correspondem a uma avaliação desejada.
     * @param prestige O critério de avaliação a ser utilizado na pesquisa.
     * @return Lista de produtos correspondentes a avaliação.
     */
    @Override
    public List<Product> getByPrestige(String prestige) {
        List<Product> productList = productRepository.getAllProducts();
        List<Product> productsPrestige = productList.stream()
                .filter(product -> product.getPrestige().equals(prestige))
                .collect(Collectors.toList());
        return productsPrestige;
    }

    /**
     * Método responsável por acionar um dos métodos de ordenação.
     * @param paramOrder O parâmentro a ser utilizado para definir qual método será chamado.
     * @return Null
     */
    @Override
    public List<Product> getAllProductsOrder(int paramOrder) {
        if(paramOrder == 0) return getAllproductsOrderByAsc();
        if(paramOrder == 1) return getAllProductsOrderByDesc();
        if(paramOrder == 2) return getAllProductsOrderByHigherPrice();
        if(paramOrder == 3) return getAllProductsOrderBySmallerPrice();
        // Verificar: tratamento para opção inválida
        return null;
    }

    /**
     * Método responsável por retornar uma lista contendo todos os produtos ordernados de forma ascendente (A-Z).
     * @return Lista dos produtos ordenados pelo nome.
     */
    private List<Product> getAllproductsOrderByAsc() {
        return getAllProducts().stream()
                .sorted(Comparator.comparing(Product::getName))
                .collect(Collectors.toList());
    }

    /**
     * Método responsável por retornar uma lista contendo todos os produtos ordernados de forma descendente (Z-A).
     * @return Lista dos produtos ordenados pelo nome.
     */
    private List<Product> getAllProductsOrderByDesc() {
        return getAllProducts().stream()
                .sorted((product01, product02) -> -product01.getName().compareTo(product02.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Método responsável por retorar uma lista contendo todos os produtos ordenados pelo MAIOR preço.
     * @return Lista de produtos ordenados pelo preço.
     */
    private List<Product> getAllProductsOrderByHigherPrice() {
        return getAllProducts().stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());
    }

    /**
     * Método responsável por retornar uma lista contendo todos os produtos ordenados pelo MENOR preço.
     * @return Lista de produtos ordenados pelo preço.
     */
    private List<Product> getAllProductsOrderBySmallerPrice() {
        return getAllProducts().stream()
                .sorted((product01, product02) -> -product01.getPrice().compareTo(product02.getPrice()))
                .collect(Collectors.toList());
    }

    /**
     * Método responsável por atualizar a quantidade de produtos em estoque.
     * @param orderRequestDtoslist Ordem de pedidos, contendo ID do produto e a quantidade
     */
    @Override
    public void updateProducts(List<OrderRequestDto> orderRequestDtoslist) {
        List<Product> productList = getAllProducts();
        List<Product> updatedProductList = productList.stream()
            .map(product -> {
                List<OrderRequestDto> founded = orderRequestDtoslist.stream()
                    .filter(order -> order.getProductId().equals(product.getProductId()))
                    .collect(Collectors.toList());

                if (founded.size() != 0) {
                    if (product.getQuantity() >= founded.get(0).getQuantity()) {
                        product.setQuantity(product.getQuantity() - founded.get(0).getQuantity());
                        return product;
                    }
    //              } else {
    //                  throw new Exception("estoque insuficiente");
    //              }
                }

                return product;
            }).collect(Collectors.toList());

        productRepository.updateProducts(updatedProductList);
    }
}
