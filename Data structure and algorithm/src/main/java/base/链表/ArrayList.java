package base.链表;

public class ArrayList<E> extends AbstractList<E>{
    /**
     * 所有元素
     */
    private E[] elements;
    private static final int DEFAULT_CAPACITY = 10;

    public ArrayList(int capaticy){
        capaticy = (capaticy > DEFAULT_CAPACITY) ? DEFAULT_CAPACITY:capaticy;
        elements = (E[]) new Object[capaticy];
    }
    public ArrayList(){this(DEFAULT_CAPACITY);}
    @Override
    public void clear() {

    }

    /**
     * 获取index元素的位置
     * @param index
     * @return
     */
    @Override
    public E get(int index) {
        rangeCheck(index);
        return elements[index];
    }

    /**
     * 设置index位置的元素
     * @param index
     * @param element
     * @return
     */
    @Override
    public E set(int index, E element) {
        rangeCheck(index);
        E Old = elements[index];
        elements[index] = element;
        return Old;
    }

    /**
     * 在index位置插入一个元素
     * @param index
     * @param element
     */
    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);
        for (int i = size; i > index ; i--) {
            elements[i] = elements[i -1];
        }
        elements[index] = element;
        size++;
    }

    /**
     * 删除index位置的元素
     * @param index
     * @return
     */
    @Override
    public E remove(int index) {
        rangeCheck(index);
        E old = elements[index];
        for (int i = index; i < size; i++) {
            elements[i - 1] = elements[i];
        }
        elements[--size] = null;
        //MC TODO:缩容
        trim();
        return old;
    }
    /**
     * 查看元素索引
     * @param element
     * @return
     */
    @Override
    public int indexOf(E element) {
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (element.equals(elements[i])) return i;
            }
        }
        return ELEMENT_NOT_FOUND;
    }

    /**
     * 保证capacity容量
     * @param capacity
     */
    private void ensureCapacity(int capacity) {
        //MC TODO:获取之前的容量
        int oldCapacity = elements.length;
        if(oldCapacity >= capacity) return;
        //MC TODO:新增容量为之前的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
        System.out.println(oldCapacity + "扩容为" + newCapacity);
    }
    private void trim() {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity >> 1;
        if(size > (newCapacity) || oldCapacity <= DEFAULT_CAPACITY) return;
        // 剩余空间还很多
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;

        System.out.println(oldCapacity + "缩容为" + newCapacity);
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
