package org.velohaven;

import org.velohaven.kaidoku.model.Grid;
import org.velohaven.kaidoku.model.Range;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Grid grid = new Grid(3, 3);

        for (Range range : grid.getColumns()) {

        }
    }
}