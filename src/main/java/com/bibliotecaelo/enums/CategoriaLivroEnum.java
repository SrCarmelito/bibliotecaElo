package com.bibliotecaelo.enums;

public enum CategoriaLivroEnum {
    ACAO_AVENTURA("Ação e aventura"),
    ARTE_FOTOGRAFIA("Arte e Fotografia"),
    AUTOAJUDA("Autoajuda"),
    BIOGRAFIA("Biografia"),
    CONTO("Conto"),
    CRIMES_REAIS("Crimes Reais"),
    DISTOPIA("Distopia"),
    ENSAIOS("Ensaios"),
    FANTASIA("Fantasia"),
    FICCAO_CIENTIFICA("Ficção Científica"),
    FICCAO_CONTEMPORANEA("Ficção Contemporânea"),
    FICCAO_FEMININA("Ficção Feminina"),
    FICCAO_HISTORICA("Ficção Histórica"),
    FICCAO_POLICIAL("Ficção Policial"),
    GASTRONOMIA("Gastronomia"),
    GRAPHIC_NOVEL("Graphic Novel"),
    GUIAS_COMO_FAZER("Guias & Como fazer"),
    HISTORIA("História"),
    HORROR("Horror"),
    HUMANIDADES_CIENCIAS_SOCIAIS("Humanidades e Ciências Sociais"),
    HUMOR("Humor"),
    INFANTIL("Infantil"),
    JOVEM_ADULTO("Jovem Adulto"),
    LGBTQ("LGBTQ"),
    MEMORIAS_AUTOBIOGRAFIA("Memórias e Autobiografia"),
    NOVELA("Novela"),
    NOVO_ADULTO("Novo Adulto"),
    PATERNIDADE_FAMILIA("Paternidade e Família"),
    REALISMO_MAGICO("Realismo Mágico"),
    RELIGIAO_ESPIRITUALIDADE("Religião e Espiritualidade"),
    ROMANCE("Romance"),
    TECNOLOGIA_CIENCIA("Tecnologia e Ciência"),
    THRILLER_SUSPENSE("Thriller e Suspense"),
    VIAGEM("Viagem");

    private String value;

    CategoriaLivroEnum(String value) {
        this.value = value;
    }

}
