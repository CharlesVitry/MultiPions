package MinMax_IA;

import Coups.Node;

import java.util.Comparator;

//operateur de difference de Node
class Operator_Node implements Comparator {

    @Override
    public int compare(Object node1, Object node2) {

        double difference = (((Node) node1).getScore() - ((Node) node2)
                .getScore());

        if (difference > 0)
            return 1;
        else if (difference == 0)
            return 0;
        else
            return -1;
    }
}
