package org.example.view;

import javax.swing.*;

public class FuncionariosView {
    public int options(){
        String[] options = {
                "0 - Adicionar todos",
                "1 - Remover João",
                "2 - Ver todos os funcionários",
                "3 - Aumentar salário em 10%",
                "4 - Agrupar em MAP",
                "5 - Ver funcionarios agrupados por função",
                "6 - Ver funcionarios que fazem aniversário nos mes 10 ou 12",
                "7 - Ver funcionário com maior idade",
                "8 - Ver funcionários em ordem alfabetica",
                "9 - Somar salários",
                "10 - Quantidade de salários mínimos",
                "\n"};

        try {
            String option = JOptionPane.showInputDialog(null, String.join("\n", options), "Qual operação deseja realizar?", JOptionPane.QUESTION_MESSAGE);
            if (option == null) return -1;
            if (option.equals("-1")) return -2;
            return Integer.parseInt(option);
        } catch (Exception _) {
            return -2;
        }
    }

    public void showFuncionarios(String[] cols, Object[][] rows) {
        JTable table = new JTable(rows, cols);
        JOptionPane.showMessageDialog(null, new JScrollPane(table), "Funcionários", JOptionPane.PLAIN_MESSAGE);
    }

    public void showPlainMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void showSucessfullMessage() {
        JOptionPane.showMessageDialog(null, "Operação realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}
