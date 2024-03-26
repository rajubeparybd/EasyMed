package com.easymed.utils;

import javafx.animation.PathTransition;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
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
}
