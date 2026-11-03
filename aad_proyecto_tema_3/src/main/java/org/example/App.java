package org.example;

import org.example.gestionProyectos.menus.ProyectoMenu;
import org.example.gestionProyectos.util.HibernateUtil;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            ProyectoMenu proyectoMenu = new ProyectoMenu(scanner);

            proyectoMenu.mostrar();

        } finally {

            HibernateUtil.shutdown();

            System.out.println("\nAplicación finalizada.");
        }
    }
}
