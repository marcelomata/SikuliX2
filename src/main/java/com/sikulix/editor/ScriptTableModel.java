/*
 * Copyright (c) 2017 - sikulix.com - MIT license
 */

package com.sikulix.editor;

import com.sikulix.core.SX;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

class ScriptTableModel extends AbstractTableModel {

  public ScriptTableModel(Script script, int maxCol, List<List<ScriptCell>> data) {
    this.data = data;
    this.maxCol = maxCol;
    this.script = script;
  }

  Script script = null;
  List<List<ScriptCell>> data = null;
  int maxCol = 1;

  public int getColumnCount() {
    return maxCol + 1;
  }

  public int getRowCount() {
    if (data.size() == 0) {
      data.add(new ArrayList<>());
    }
    return data.size();
  }

  public String getColumnName(int col) {
    if (col == 0) return "    Line";
    if (col == 1) return "Command";
    return String.format("Item%d", col - 1);
  }

  public Object getValueAt(int row, int col) {
    if (col == 0) {
      return String.format("%6d %s", row + 1, script.cellAt(row, 2).getIndentMarker());
    }
    if (row > data.size() - 1) {
      return "";
    }
    int lineCol = col - 1;
    List<ScriptCell> line = data.get(row);
    if (lineCol > line.size() - 1) {
      return "";
    }
    return data.get(row).get(lineCol).get();
  }

  public Class getColumnClass(int c) {
    return String.class;
  }

  public boolean isCellEditable(int row, int col) {
    return true;
  }

  public void setValueAt(Object value, int row, int col) {
    if (row < 0 && col < 0) {
      fireTableDataChanged();
      return;
    }
    if (SX.isNull(value)) {
      script.checkContent(row);
      fireTableDataChanged();
      script.getTable().setSelection(row, 2);
      return;
    }
    String given = ((String) value).trim();
    if (col == 0) {
      return;
    }
    script.cellAt(row, col).set(given);
    fireTableCellUpdated(row, col);
  }
}
