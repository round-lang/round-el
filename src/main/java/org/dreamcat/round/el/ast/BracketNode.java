package org.dreamcat.round.el.ast;

import java.util.ArrayList;
import java.util.List;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.lex.OperatorToken;

/**
 * @author Jerry Will
 * @version 2021-08-15
 */
public class BracketNode extends ElNode {

    public List<ElNode> vector;
    public List<List<ElNode>> matrix;
    // transient
    public List<ElNode> currentRow;

    BracketNode() {
        vector = new ArrayList<>();
    }

    BracketNode(List<List<ElNode>> matrix) {
        this.matrix = matrix;
    }

    @Override
    boolean isOperator() {
        return true;
    }

    @Override
    OperatorToken getOperator() {
        return OperatorToken.BRACKET;
    }

    @Override
    void addChild(ElNode child) {
        if (vector == null) {
            currentRow.add(child);
        } else {
            vector.add(child);
        }
        child.parent = this;
    }

    @Override
    void removeChild(ElNode child) {
        if (vector == null) {
            currentRow.remove(child);
        } else {
            vector.remove(child);
        }
    }

    @Override
    public ElNode copy() {
        if (matrix != null) {
            List<List<ElNode>> m = new ArrayList<>();
            for (List<ElNode> v : matrix) {
                List<ElNode> row = new ArrayList<>();
                for (ElNode i : v) {
                    row.add(i.copy());
                }
                m.add(row);
            }
            return new BracketNode(m);
        }

        BracketNode node = new BracketNode();
        for (ElNode i : vector) {
            node.vector.add(i.copy());
        }
        return node;
    }

    void nextRow() {
        if (matrix == null) {
            matrix = new ArrayList<>();
            matrix.add(vector);
            vector = null;
        }
        if (currentRow != null) {
            matrix.add(currentRow);
        }
        currentRow = new ArrayList<>();
    }

    void clearRow() {
        if (currentRow != null) {
            if (!currentRow.isEmpty()) {
                matrix.add(currentRow);
            }
            currentRow = null;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        if (matrix != null) {
            s.append("[\n");
            for (List<ElNode> v : matrix) {
                int size = v.size();
                if (size >= 1) {
                    s.append(v.get(0));
                    for (int i = 1; i < size; i++) {
                        s.append(", ").append(v.get(i));
                    }
                }
                s.append("\n");
            }
            return s.append("]").toString();
        }

        s.append("[");
        int size = vector.size();
        if (size >= 1) {
            s.append(vector.get(0));
            for (int i = 1; i < size; i++) {
                s.append(", ").append(vector.get(i));
            }
        }
        return s.append("]").toString();
    }

    @Override
    public Object evaluate(ElContext context, ElEngine engine) {
        if (matrix != null) {
            List<List<Object>> list = new ArrayList<>(matrix.size());
            for (List<ElNode> v : matrix) {
                List<Object> a = new ArrayList<>(v.size());
                for (ElNode node : v) {
                    Object value = node.evaluate(context, engine);
                    a.add(value);
                }
                list.add(a);
            }
            return list;
        }

        List<Object> list = new ArrayList<>(vector.size());
        for (ElNode node : vector) {
            Object value = node.evaluate(context, engine);
            list.add(value);
        }
        return list;
    }
}
