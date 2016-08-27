import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class BitArray implements Iterable<Boolean> {	
	private int _length;
	private long[] _data = null;
	private static final short SIZE_OF_LONG = 64; 
	
	public BitArray(int length){
		this._data = new long[length / SIZE_OF_LONG + 1];
		this._length = length;
	}
	
	public BitArray(int length, boolean $default){
		this._data = new long[length / SIZE_OF_LONG + 1];
		this._length = length;
		if($default) for(int i = 0; i < _data.length; i++) _data[i] = -1L;
	}
	
	public int length(){
		return this._length;
	}
	
	public void set(int index, boolean value) throws IndexOutOfBoundsException {
		if(index >= _length || index < 0) throw new IndexOutOfBoundsException("index was outside the length of the array");
		
		int integerIndex = index / SIZE_OF_LONG;
		int bitIndex = index % SIZE_OF_LONG;
		
		long currentValue = _data[integerIndex];
		if(value){
			currentValue |= MASKS.get(bitIndex);
		} else {
			currentValue &= ~MASKS.get(bitIndex);
		}
		_data[integerIndex] = currentValue;
	}
	
	public boolean get(int index) throws IndexOutOfBoundsException {
		if(index >= _length || index < 0) throw new IndexOutOfBoundsException("index was outside the length of the array");
		
		int integerIndex = index / SIZE_OF_LONG;
		int bitIndex = index % SIZE_OF_LONG;
		
		long currentValue = _data[integerIndex];
		return (currentValue & MASKS.get(bitIndex)) == MASKS.get(bitIndex);
	}
	
	
	private static final Map<Integer, Long> MASKS = new HashMap<Integer, Long>(); 
	static {
		for (int i = 0; i < SIZE_OF_LONG; i++){
			MASKS.put(i, 1L << i);
		}
	}
	@Override
	public Iterator<Boolean> iterator() {
		return new BitArrayIterator(this);
	}
	
	class BitArrayIterator implements Iterator<Boolean> {

		private int index = 0;
		private BitArray array;
		
		BitArrayIterator(BitArray iterable){
			this.array = iterable;
		}
		
		@Override
		public boolean hasNext() {
			return index < this.array._length;
		}

		@Override
		public Boolean next() {
			try{
				return this.array.get(index++);
			} catch (IndexOutOfBoundsException ex){
				throw new NoSuchElementException(ex.getMessage());
			}
		}
	}
}
