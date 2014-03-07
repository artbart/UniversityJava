import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by artbart on 3/7/14.
 */
public class TestLinkedDeque {
    @Test
    public void testSimpleOperations() throws Exception {
        LinkedDeque linkedDeque=new LinkedDeque();

        Assert.assertEquals("isEmpty new list",true,linkedDeque.isEmpty());

        linkedDeque.add(0);
        linkedDeque.add(1);
        linkedDeque.add(2);

        Assert.assertEquals("isEmpty after add",false,linkedDeque.isEmpty());
        Assert.assertEquals("size",3,linkedDeque.size());

        Assert.assertEquals("contains 0",true,linkedDeque.contains(0));
        Assert.assertEquals("contains 3",false,linkedDeque.contains(3));

        Assert.assertEquals("remove 0",true,linkedDeque.remove(0));
        Assert.assertEquals("remove 3", false, linkedDeque.remove(3));
        Assert.assertEquals("size after remove ",2,linkedDeque.size());

        linkedDeque.clear();
        Assert.assertEquals("isEmpty after clear",true,linkedDeque.isEmpty());

    }

    @Test
    public void testBulkOperations() throws Exception {
        LinkedDeque linkedDeque=new LinkedDeque();
        ArrayList<Integer> toAdd=new ArrayList<>();
        toAdd.add(1);
        toAdd.add(2);
        toAdd.add(3);
        toAdd.add(4);
        toAdd.add(5);
        toAdd.add(6);
        linkedDeque.addAll(toAdd);

        Assert.assertEquals("size after add", toAdd.size(), linkedDeque.size());

        Iterator itld=linkedDeque.iterator();
        Iterator ittoAdd=toAdd.iterator();
        while (itld.hasNext()){
            Assert.assertEquals("vals should be the same",ittoAdd.next(),itld.next());
        }

        ArrayList<Integer> toRetain=new ArrayList<>();
        toRetain.add(1);
        toRetain.add(3);
        toRetain.add(4);
        toRetain.add(6);
        linkedDeque.retainAll(toRetain);
        Assert.assertEquals("size after retain", 4, linkedDeque.size());

        itld=linkedDeque.iterator();
        Iterator itToRetain=toRetain.iterator();

        while (itld.hasNext()){
            Assert.assertEquals("vals should be the same", itToRetain.next(),itld.next() );
        }

        ArrayList<Integer> toContains1=new ArrayList<>();
        toContains1.add(1);
        toContains1.add(3);
        toContains1.add(4);
        toContains1.add(6);
        Assert.assertEquals("contains all", true, linkedDeque.containsAll(toContains1));

        ArrayList<Integer> toContains2=new ArrayList<>();
        toContains2.add(1);
        toContains2.add(10);
        Assert.assertEquals("contains all", false, linkedDeque.containsAll(toContains2));


        ArrayList<Integer> toRemove=new ArrayList<>();
        toRemove.add(1);
        toRemove.add(4);
        linkedDeque.removeAll(toRemove);
        Assert.assertEquals("size after remove all", 2, linkedDeque.size());

        itld=linkedDeque.iterator();
        Assert.assertEquals("after removeAll 1-st", 3, itld.next());
        Assert.assertEquals("after removeAll 2", 6, itld.next());
    }

    @Test
    public void testToArray() throws Exception {
        LinkedDeque linkedDeque=new LinkedDeque();
        Integer[] arr=new Integer[]{1,2,3,4};
        linkedDeque.add(1);
        linkedDeque.add(2);
        linkedDeque.add(3);
        linkedDeque.add(4);

        Object[] newArr=linkedDeque.toArray();
        Integer[] newArrI=linkedDeque.toArray(new Integer[4]);
        for (int i=0; i<arr.length; i++){
            Assert.assertEquals(arr[i],newArr[i]);
            Assert.assertEquals(arr[i],newArrI[i]);
        }
    }

    @Test
    public void testDequeOperations() throws Exception {
        LinkedDeque linkedDeque=new LinkedDeque();
        linkedDeque.addFirst(1);
        linkedDeque.addFirst(2);
        linkedDeque.addFirst(3);
        Assert.assertEquals("getF", 3, (int)linkedDeque.getFirst());
        Assert.assertEquals("removeF 1", 3, (int)linkedDeque.removeFirst());
        Assert.assertEquals("removeF 2", 2, (int)linkedDeque.removeFirst());
        Assert.assertEquals("removeF 3", 1, (int)linkedDeque.removeFirst());

        linkedDeque.addLast(1);
        linkedDeque.addLast(2);
        linkedDeque.addLast(3);
        Assert.assertEquals("getL", 3, (int)linkedDeque.getLast());
        Assert.assertEquals("removeL 1", 3, (int)linkedDeque.removeLast());
        Assert.assertEquals("removeL 2", 2, (int)linkedDeque.removeLast());
        Assert.assertEquals("removeL 3", 1, (int)linkedDeque.removeLast());
    }


}
