import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String order;
        int number;
        SkipList skipList = new SkipList();
        while (scanner.hasNext()) {
            order = scanner.next();
            if (order.equals("Insert")) {
                number = scanner.nextInt();
                skipList.insert(number);
                continue;
            }
            if (order.equals("Delete")) {
                number = scanner.nextInt();
                skipList.delete(number);
                continue;
            }
            if (order.equals("Search")) {
                number = scanner.nextInt();
                System.out.println(skipList.search(number));
                continue;
            }
            if (order.equals("Print")) {
                skipList.print();
            }
        }
    }
}


class SkipList {
    private int depth = 0;
    private Node startNode = new Node(Integer.MIN_VALUE);
    private Node currentStartNode;

    {
        currentStartNode = startNode;
        Node temp;
        Node node = this.currentStartNode;
        for (int i = 0; i < 9; i++) {
            temp = new Node(Integer.MIN_VALUE);
            temp.down = node;
            node.up = temp;
            node = node.up;
        }
    }

    private Random random = new Random();

    private Node[] setDefaultArray() {
        Node[] nodes = new Node[10];
        Node node = startNode;
        for (int i = 0; i < 10; i++) {
            nodes[i] = node;
            node = node.up;
        }
        return nodes;
    }

    public void insert(int number) {
        if (this.currentStartNode.next == null) {
            this.currentStartNode.next = new Node(number);
            currentStartNode.next.pre = this.currentStartNode;
            return;
        }
        Node[] nodes = setDefaultArray();
        int i = this.depth;
        Node node = this.currentStartNode.next;
        while (true) {
            while (node.key < number) {
                if (node.next == null)
                    break;
                node = node.next;
            }
            if (node.key >= number)
                node = node.pre;
            nodes[i] = node;
            i--;
            if (node.down == null)
                break;
            node = node.down;
        }
        if (node.key > number)
            node = node.pre;
        Node temp = new Node(number);
        temp.pre = node;
        temp.next = node.next;
        node.next = temp;
        if (temp.next != null)
            temp.next.pre = temp;
        node = temp;
        int randomNumber;
        int counter = 0;
        while (counter <= depth) {
            if (counter == 9)
                break;
            randomNumber = random.nextInt();
            if (randomNumber < 0)
                randomNumber = -randomNumber;
            if (randomNumber % 4 != 0) {
                break;
            } else {
                temp = new Node(number);
                temp.down = node;
                node.up = temp;
                node = node.up;
                if (counter != depth) {
                    temp.pre = nodes[counter + 1];
                    temp.next = nodes[counter + 1].next;
                    nodes[counter + 1].next = temp;
                    if (temp.next != null)
                        temp.next.pre = temp;
                } else {
                    this.currentStartNode = this.currentStartNode.up;
                    this.currentStartNode.next = temp;
                    temp.pre = this.currentStartNode;
                    this.depth++;
                    return;
                }
            }
            counter++;
        }
    }

    public boolean search(int number) {
        Node node = this.currentStartNode.next;
        if (node == null)
            return false;
        while (true) {
            while (node.key < number) {
                if (node.next == null)
                    break;
                node = node.next;
            }
            if (node.key == number)
                return true;
            if (node.key > number)
                node = node.pre;
            if (node.down == null)
                break;
            node = node.down;
        }
        if ((node.next != null && node.next.key == number))
            return true;
        return false;
    }

    public void delete(int number) {
        Node node = this.currentStartNode.next;
        if (node == null) {
            System.out.println("error");
            return;
        }
        while (true) {
            while (node.key < number) {
                if (node.next == null)
                    break;
                node = node.next;
            }
            if (node.key >= number)
                node = node.pre;
            if (node.down == null)
                break;
            node = node.down;
        }
        Node node1 = null;
        if (node.next == null) {
            System.out.println("error");
            return;
        }
        if ((node.next.key != number)) {
            System.out.println("error");
            return;
        }
        node = node.next;
        while (node.key == number) {
            node.pre.next = node.next;
            if (node.next != null)
                node.next.pre = node.pre;
            node1 = node;
            int counter = 0;
            while (node1.up != null) {
                node1 = node1.up;
                counter++;
                if (node1.pre.key == Integer.MIN_VALUE && node1.next == null) {
                    this.currentStartNode.next = null;
                    if (depth == 0)
                        break;
                    if (counter == this.depth && this.currentStartNode.down!=null) {
                        this.currentStartNode = this.currentStartNode.down;
                        depth--;
                        while (this.currentStartNode.next == null) {
                            if (this.currentStartNode.down == null)
                                break;
                            this.currentStartNode = this.currentStartNode.down;
                            this.depth--;
                        }
                        break;
                    }
                }
                node1.pre.next = node1.next;
                if (node1.next != null)
                    node1.next.pre = node1.pre;
            }
            if (node.next == null)
                break;
            node = node.next;
        }
    }

    public void print() {
        if (this.startNode.next == null) {
            System.out.println("empty");
            return;
        }
        Node node = this.startNode.next;
        while (node != null) {
            System.out.print(node.key + " ");
            node = node.next;
        }
        System.out.println();
    }
}


class Node {
    Node up;
    Node down;
    Node next;
    Node pre;
    int key;

    public Node(int number) {
        up = null;
        down = null;
        next = null;
        pre = null;
        key = number;
    }
}