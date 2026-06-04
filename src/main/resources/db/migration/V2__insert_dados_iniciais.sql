insert into mesas (numero, descricao, capacidade) values
                                                      (1, 'Mesa proximo a entrada', 4),
                                                      (2, 'Mesa central', 4),
                                                      (3, 'Mesa proxima a janela', 2),
                                                      (4, 'Mesa familia', 6),
                                                      (5, 'Mesa externa', 4);

insert into categorias_produtos (nome) values
                                           ('Entradas'),
                                           ('Pratos Principais'),
                                           ('Bebidas'),
                                           ('Sobremesas');

insert into produtos (categoria_id, nome, descricao, preco, tempo_preparo_minutos)
select id, 'Batata Frita', 'Porcao de batata frita crocante', 28.90, 15
from categorias_produtos where nome = 'Entradas';

insert into produtos (categoria_id, nome, descricao, preco, tempo_preparo_minutos)
select id, 'X-Burguer Artesanal', 'Hamburguer artesanal com queijo e molho especial', 34.90, 30
from categorias_produtos where nome = 'Pratos Principais';

insert into produtos (categoria_id, nome, descricao, preco, tempo_preparo_minutos)
select id, 'File com Fritas', 'File grelhado acompanhado de fritas', 59.90, 30
from categorias_produtos where nome = 'Pratos Principais';

insert into produtos (categoria_id, nome, descricao, preco, tempo_preparo_minutos)
select id, 'Suco Natural', 'Suco natural da fruta', 12.00, 5
from categorias_produtos where nome = 'Bebidas';

insert into produtos (categoria_id, nome, descricao, preco, tempo_preparo_minutos)
select id, 'Pudim', 'Pudim tradicional da casa', 14.90, 5
from categorias_produtos where nome = 'Sobremesas';