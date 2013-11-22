public class DMain {
	public static void main(String[] args) {
		DHeap heap = new DHeap(2);
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < alphabet.length(); i++) {
			Vertex v = new Vertex("" + alphabet.charAt(i));
			v.setCost(i);
			heap.insert(v);
		}
		Vertex max = new Vertex("zed");
		max.setCost(50);
		heap.insert(max);
		heap.decreaseKey(max, 0);

		while (!heap.isEmpty())
			System.out.println(heap.deleteMin());
	}
}