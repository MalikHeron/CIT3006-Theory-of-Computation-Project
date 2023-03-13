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

    //Create showData() method for displaying data of doubly linked list
    fun showData() : String {
        //Initialize a new node current that will point to head
        var current = head
        //Check whether the doubly linked list is empty or not
        if (head == null) {
            //Print a statement and pass the control flow into the main() method
            println("List is empty")
            return ""
        }
        //Print a statement
        print("Nodes of doubly linked list: ")
        //Iterate the doubly linked list using while
        while (current != null) {
            //Print tha data on that particular node and then increment the pointer for indicating next node
            print(current.data)
            current = current.next
        }
        return ""
    }

    fun getCurrent(): Node? {
        return head
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val obj = DoublyLinkedList()

            /*Add nodes into the doubly linked list
            obj.addNewNode("New York")
            obj.addNewNode("Los Angeles")
            obj.addNewNode("Chicago")
            obj.addNewNode("Houston")
            obj.addNewNode("Houston")*/

            //Call showData() method for displaying doubly linked list data
            obj.showData()
        }
    }
}