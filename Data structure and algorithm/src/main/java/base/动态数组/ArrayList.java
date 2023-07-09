package base.动态数组;

import java.util.Arrays;

public class ArrayList<E> {
    /**
     * 元素数量
     */
    private int size;
    /**
     * 所有元素
     */
    private E[] elements;

    private static final int DEFAULT_CAPACITY = 10;
    private static final int ELEMENT_NOT_FOUND = -1;

    public ArrayList(int capaticy){
        capaticy = (capaticy < DEFAULT_CAPACITY) ? DEFAULT_CAPACITY:capaticy;
        elements = (E[]) new Object[capaticy];
    }
    public ArrayList(){
        this(DEFAULT_CAPACITY);
    }

    /**
     * 清除所有元素
     */
    public void clear(){
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * 元素数量
     * @return
     */
    public int size(){
        return  size;
    }

    /**
     * 是否为空
     * @return
     */
    public boolean isEmpty(){return size == 0;}

    /**
     * 是否包含元素
     * E是代表的类型 String例如
     * @param element
     * @return
     */
    public boolean contains(E element){return indexOf(element) != ELEMENT_NOT_FOUND;}

    /**
     * 添加元素到尾部
     * @param element
     */
    public void add(E element){add(size,element);}

    /**
     * 判断是否包含某元素
     * @param element
     * @return
     */
    private int indexOf(E element) {
        if(element == null){
            for (int i = 0; i < size; i++) {
                if(elements[i] == null) return i;
            }
        }else {
            for (int i = 0; i < size; i++) {
                if (element.equals(elements[i])) return i; // n
            }
        }
        return ELEMENT_NOT_FOUND;
    }

    /**
     * 在index位置插入一个元素
     * 3 4 6 7 8
     * 0 1 2 3 4
     *
     * 3 4 5 6 7 8
     * 0 1 2 3 4 5
     * @param index
     * @param element
     */
    public void add(int index,E element){
        // TODO: 判断是否在范围
        rangeCheckForAdd(index);
        //meicilly TODO:判断容量
        ensureCapacity(size + 1);
        for (int i = size; i > index ; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    /**
     * 获取index位置的元素
     * @param index
     * @return
     */
    public E get(int index){
        //meicilly TODO:范围检查
        rangeCheck(index);
        return elements[index];
    }
    /**
     * 设置index位置元素
     * @param index
     * @param element
     * @return 原来的元素
     */
    public E set(int index,E element){
        rangeCheck(index);

        E old = elements[index];
        elements[index] = element;
        return old;
    }

    /**
     * 删除index位置的元素
     * @param index
     * @return
     */
    public E remove(int index){
        E old = elements[index];
        for (int i = index + 1; i < size; i++) {
            elements[i - 1] = elements[i];
        }
        elements[--size] = null;
        return old;
    }
    /**
     * 范围的检查
     * @param index
     */
    private void rangeCheck(int index) {
        if(index < 0 || index > size){
            outOfBounds(index);
        }
    }

    /**
     * 保证要有容量
     * @param capacity
     */
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if(oldCapacity >= capacity) return;
        //meicilly TODO:新容量是旧容量的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
        System.out.println(oldCapacity + "扩容为" + newCapacity);
    }

    /**
     * 判断是否在范围
     * @param index
     */
    private void rangeCheckForAdd(int index) {
        if(index < 0 || index > size){
            outOfBounds(index);
        }
    }

    /**
     * 抛出异常
     * @param index
     */
    private void outOfBounds(int index) {
        throw new IndexOutOfBoundsException("Index:" + index + ", Size:" + size);
    }

    @Override
    public String toString() {
        // size=3, [99, 88, 77]
        StringBuilder string = new StringBuilder();
        string.append("size=").append(size).append(", [");
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                string.append(", ");
            }

            string.append(elements[i]);

//			if (i != size - 1) {
//				string.append(", ");
//			}
        }
        string.append("]");
        return string.toString();
    }
}
