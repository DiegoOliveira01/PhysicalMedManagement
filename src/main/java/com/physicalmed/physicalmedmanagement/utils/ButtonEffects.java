package com.physicalmed.physicalmedmanagement.utils;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * Classe utilitária que aplica efeitos visuais em botões ou outros Nodes do JavaFX.
 */


public class ButtonEffects {

    /**
     * Aplica efeito de "pressionado" em qualquer Node (ex: Button, ImageView, etc.).
     * Quando o mouse é pressionado, o elemento diminui conforme os valores de escala.
     * Ao soltar, o elemento volta ao tamanho normal.
     *
     * @param node   o elemento que receberá o efeito
     * @param scaleX fator de escala horizontal quando pressionado
     * @param scaleY fator de escala vertical quando pressionado
     */

    public static void applyPressEffect(Node node, double scaleX, double scaleY) {
        node.setOnMousePressed(e -> {
            node.setScaleX(scaleX);
            node.setScaleY(scaleY);
        });

        node.setOnMouseReleased(e -> {
            node.setScaleX(1);
            node.setScaleY(1);
        });
    }

    /**
     * Aplica efeito de "hover" em qualquer Node (ex: Button, ImageView, etc.).
     * Quando o mouse entra, o elemento aumenta conforme os valores de escala.
     * Quando o mouse sai, o elemento volta ao tamanho normal.
     *
     * @param node   o elemento que receberá o efeito
     * @param scaleX fator de escala horizontal quando o mouse entra
     * @param scaleY fator de escala vertical quando o mouse entra
     */

    public static void applyHoverEffect(Node node, double scaleX, double scaleY) {

        node.setOnMouseEntered(e -> {
            node.setScaleX(scaleX);
            node.setScaleY(scaleY);
        });

        node.setOnMouseExited(e -> {
            node.setScaleX(1);
            node.setScaleY(1);
        });
    }

}
