package util

class DoublyLinkedList {
    //Initialize head and tail for the doubly linked list
    private var head: Node? = null
    private var tail: Node? = null

    //Add a node into a list
    fun addNewNode(data: Char) {
        //Create node
        val newNode = Node(data)

        //Check whether our doubly linked list is empty or not
        if (head == null) {
            //The newNode is pointed by both head or tail
            head = newNode
            tail = newNode
            //It is first node so prev will point to null
            head!!.prev = null
            //It is also last node so tail's next will point to null
            tail!!.next = null
        } else {
            //The newly created node will be the last node, so now tail's next will point to that newly created node
            tail!!.next = newNode
            //The tail is pointing to the second last node so the newly created node's prev will point to tail
            newNode.prev = tail
            //The newly created node will become new tail because it is last node in the doubly linked list
            tail = newNode
            //The newly created node will be the last node so tail's next will be null
            tail!!.next = null
        }
    }

    fun getData(): String {
        //Initialize a new node current that will point to head
        var current = head
        //Store output
        var data = ""
        //Check whether the doubly linked list is empty or not
        if (head == null) {
            return ""
        }
        //Iterate the doubly linked list using while
        while (current != null) {
            data += current.data
            current = current.next
        }
        return data
    }

    fun getCurrent(): Node? {
        return head
    }
}