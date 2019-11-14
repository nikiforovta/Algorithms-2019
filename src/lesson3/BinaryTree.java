package lesson3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private NewSuperSet headSet;
    private NewSuperSet tailSet;
    private NewSuperSet subSet;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        updateSets();
        return true;
    }

    private void updateSets() {
        if (tailSet != null) {
            tailSet(tailSet.getFromElement());
        }
        if (headSet != null) {
            headSet(headSet.getToElement());
        }
        if (subSet != null) {
            subSet(subSet.getFromElement(), subSet.getToElement());
        }
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    public int height() {
        return height(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    /**
     * Time complexity: O(h)
     * Space complexity: O(1)
     */
    @Override
    public boolean remove(Object o) {
        if (o == null || !contains(o)) {
            return false;
        }
        root = recursiveRemove(root, new Node<>((T) o));
        size--;
        updateSets();
        return true;
    }

    private Node<T> recursiveRemove(Node<T> root, Node<T> toRemove) {
        if (root == null) {
            return null;
        }

        int comparison = toRemove.value.compareTo(root.value);
        if (comparison < 0) {
            root.left = recursiveRemove(root.left, toRemove);
        } else {
            if (comparison > 0) {
                root.right = recursiveRemove(root.right, toRemove);

            } else {
                if (root.left != null && root.right != null) {
                    Node<T> node = new Node<>(minValue(root.right));
                    node.left = root.left;
                    node.right = root.right;
                    root = node;
                    root.right = recursiveRemove(root.right, root);
                } else {
                    if (root.left != null) {
                        root = root.left;
                    } else {
                        root = root.right;
                    }
                }
            }
        }
        return root;
    }

    private T minValue(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.value;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        Stack<Node<T>> nodes;
        private Node<T> current = null;

        private BinaryTreeIterator() {
            nodes = new Stack<>();
            leftmostInorder(root);
        }

        private void leftmostInorder(Node<T> root) {
            while (root != null) {
                nodes.push(root);
                root = root.left;
            }
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         */
        /**
         * Time Complexity: O(1)
         * Space Complexity: O(h)
         */
        @Override
        public boolean hasNext() {
            return nodes.size() > 0;
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */
        /**
         * Time Complexity: O(1)
         * все узлы бинарного дерева
         * Space Complexity: O(h)
         */
        @Override
        public T next() {
            current = nodes.pop();
            if (current.right != null) {
                leftmostInorder(current.right);
            }
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        /**
         * Time complexity: O(h)
         * Space complexity: O(1)
         */
        @Override
        public void remove() {
            if (current != null) {
                root = recursiveRemove(root, current);
                size--;
            }
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    class NewSuperSet extends TreeSet {
        private T fromElement;
        private T toElement;

        T getFromElement() {
            return fromElement;
        }

        T getToElement() {
            return toElement;
        }

        void setFromElement(T fromElement) {
            this.fromElement = fromElement;
        }

        void setToElement(T toElement) {
            this.toElement = toElement;
        }

        NewSuperSet(T fromElement, T toElement) {
            this.fromElement = fromElement;
            this.toElement = toElement;
        }

        @Override
        public boolean add(Object o) {
            T value = (T) o;
            if (value == null) {
                return false;
            }
            if (toElement == null) {
                if (value.compareTo(fromElement) < 0) {
                    throw new IllegalArgumentException();
                } else {
                    BinaryTree.this.add(value);
                    super.add(value);
                    return true;
                }
            }
            if (fromElement == null) {
                if (value.compareTo(toElement) >= 0) {
                    throw new IllegalArgumentException();
                } else {
                    BinaryTree.this.add(value);
                    super.add(value);
                    return true;
                }
            }
            if (value.compareTo(toElement) < 0 && value.compareTo(fromElement) >= 0) {
                BinaryTree.this.add(value);
                super.add(value);
            } else {
                throw new IllegalArgumentException();
            }
            return true;
        }

        @Override
        public boolean remove(Object o) {
            BinaryTree.this.remove(o);
            return super.remove(o);
        }
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    /**
     * Time complexity: O(n)
     * Space complexity: O(1)
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        if (fromElement.compareTo(toElement) >= 0) {
            throw new IllegalArgumentException();
        }
        if (subSet == null) {
            subSet = new NewSuperSet(fromElement, toElement);
        }
        if (subSet.getToElement() != null && subSet.getToElement().compareTo(toElement) != 0) {
            subSet.setToElement(toElement);
        }
        if (subSet.getFromElement() != null && subSet.getFromElement().compareTo(fromElement) != 0) {
            subSet.setFromElement(fromElement);
        }
        subSet.clear();
        for (T value : this) {
            if (value.compareTo(toElement) < 0 && value.compareTo(fromElement) >= 0) {
                subSet.add(value);
            }
        }
        return subSet;
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    /**
     * Time complexity: O(n)
     * Space complexity: O(1)
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        if (headSet == null) {
            headSet = new NewSuperSet(null, toElement);
        }
        if (headSet.getToElement() != null && headSet.getToElement().compareTo(toElement) != 0) {
            headSet.setToElement(toElement);
        }
        headSet.clear();
        for (T value : this) {
            if (value.compareTo(toElement) < 0) {
                headSet.add(value);
            } else {
                break;
            }
        }
        return headSet;
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    /**
     * Time complexity: O(n)
     * Space complexity: O(1)
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        if (tailSet == null) {
            tailSet = new NewSuperSet(fromElement, null);
        }
        if (tailSet.getFromElement() != null && tailSet.getFromElement().compareTo(fromElement) != 0) {
            tailSet.setFromElement(fromElement);
        }
        tailSet.clear();
        for (T value : this) {
            if (value.compareTo(fromElement) >= 0) {
                tailSet.add(value);
            }
        }
        return tailSet;
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}