package ru.geekbrains.catch_the_drop;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {
    //создание переменных для проекта
    private static long last_frame_time; //время запуска окна
    private static Image background; //img
    private static Image game_over;//img
    private static Image drop;//img
    private static GameWindow game_window; //okno
    private static float drop_left = 200; //координаты выводы капли по оси y
    private static float drop_top = -100; // координаты выводы капли по оси x
    private static float drop_v = 200; // speed drop
    private static int score; //очки

    public static void main(String[] args) throws IOException {
        //подключаем изображения в нужные места
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
        //создаем окно
        game_window = new GameWindow();
        //выключаем игру при закрытии окна
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //координаты вывода окна
        game_window.setLocation(200,100);
        //размер окна
        game_window.setSize(906,478);
        //запрещаем изменять размер
        game_window.setResizable(false);
        //устанавливаем время в переменную
        last_frame_time = System.nanoTime();
        //создаем игровое поле
        GameField game_field = new GameField();
        //подключаем работу с мышкой
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            //ловим координаты клика
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int  y = e.getY();
                //узнаем координаты капли
                float drop_right = drop_left+drop.getWidth(null);
                float drop_bottom = drop_top+drop.getHeight(null);
                //проверяем попали ли мышкой
                boolean is_drop = x >= drop_left && x<= drop_right && y >= drop_top && y <= drop_bottom;
                if (is_drop) {
                    //если попали мышкой то + 1 к очкам и откидываем каплю рандомно
                    drop_top = -100;
                    drop_left = (int)(Math.random() * game_field.getWidth() - drop.getWidth(null));
                    drop_v  = drop_v + 20;
                    score++;
                    game_window.setTitle("Score: " + score);
                }
            }
        });
        //делаем видимое окно
        game_window.add(game_field);
        game_window.setVisible(true);
    }
    private static void onRepaint(Graphics g) {
        //движение капли
        long current_time = System.nanoTime();
        float delta_time = (current_time-last_frame_time)*0.000000001f;
        last_frame_time = current_time;
        drop_top = drop_top+drop_v*delta_time;

        g.drawImage(background,0,0,null);
        g.drawImage(drop,(int) drop_left,(int) drop_top,null);
        //если капля ушла вниз окна то выводим Game Over
        if (drop_top > game_window.getHeight()) g.drawImage(game_over,280,(int) 120,null);
    }
    private static class GameField extends JPanel{
        @Override
        protected void paintComponent (Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }

    }
}