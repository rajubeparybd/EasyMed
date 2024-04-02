package com.easymed.utils;

import javafx.animation.PathTransition;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

/**
 * Transitions class is used to create different types of transitions for nodes.
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class Transitions {

    /**
     * circleTransition method is used to create a circular transition for a node.
     *
     * @param node     Node to be transitioned.
     * @param radius   Radius of the circle.
     * @param duration Duration of the transition.
     */
    public static void circleTransition(Node node, double radius, double duration) {
        Circle circle = new Circle(radius);
        PathTransition transition = new PathTransition();
        transition.setNode(node);
        transition.setDuration(Duration.seconds(duration));
        transition.setPath(circle);
        transition.setCycleCount(PathTransition.INDEFINITE);
        transition.play();
    }

    public static void changeSVG(BorderPane rootPane, SVGPath svg, ImageView doctorImg) {
        double height = rootPane.getScene().getHeight();
        double width = rootPane.getScene().getWidth();
        double lx = 344;
        double cx = 344;
        double rx = 571.04;
        double ty = width / 2;
        String path = "M 0 " + Math.round(lx) + " C 0 " + Math.round(cx) + " 435 " + Math.round(rx) + " " + Math.round(ty) + " 0 L " + Math.round(ty) + " " + Math.round(height) + " L 0 " + Math.round(height) + " Z M 0 344.1211";
        svg.setContent(path);
        doctorImg.setLayoutX(ty - 247);
    }
}
