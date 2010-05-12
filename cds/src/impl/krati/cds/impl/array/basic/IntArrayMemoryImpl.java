package krati.cds.impl.array.basic;

import java.io.IOException;

import krati.cds.array.IntArray;

public class IntArrayMemoryImpl extends AbstractArray<int[]> implements IntArray
{
    protected long _lwmScn = 0; // Low water mark SCN starts from 0
    protected long _hwmScn = 0; // High water mark SCN starts from 0
    
    public IntArrayMemoryImpl(int memberIdStart, int memberIdCount)
    {
        super(memberIdStart, memberIdCount);
    }

    @Override
    protected void init() 
    { 
        _internalArray = new int[_memberIdCount];
    }

    @Override
    public int getData(int index)
    {
      return _internalArray[index - _memberIdStart];
    }

    @Override
    public void setData(int index, int value, long scn) throws Exception
    {
      _internalArray[index - _memberIdStart] = value;
      _hwmScn = Math.max(_hwmScn, scn);
    }
    
    @Override
    public void clear()
    {
        if (_internalArray != null)
        {
          for (int i = 0; i < _internalArray.length; i ++)
          {
            _internalArray[i] = 0;
          }
        }
    }
    
    @Override
    public int getIndexStart()
    {
        return _memberIdStart;
    }

    @Override
    public boolean indexInRange(int index)
    {
        return (_memberIdStart <= index && index < _memberIdEnd);
    }

    @Override
    public int length()
    {
        return _memberIdCount;
    }

    @Override
    public long getHWMark()
    {
        return _hwmScn;
    }

    @Override
    public long getLWMark()
    {
        return _lwmScn;
    }

    @Override
    public void sync() throws IOException
    {
        // not supported for memory-based IntArray implementation
    }
    
    @Override
    public void persist() throws IOException
    {
        // not supported for memory-based IntArray implementation
    }
    
    @Override
    public void saveHWMark(long endOfPeriod) throws Exception
    {
        _hwmScn = endOfPeriod;
        _lwmScn = endOfPeriod;
    }

    @Override
    public Object memoryClone()
    {
        IntArrayMemoryImpl memClone = new IntArrayMemoryImpl(getIndexStart(), length());
        
        System.arraycopy(_internalArray, 0, memClone.getInternalArray(), 0, _internalArray.length);
        memClone._lwmScn = getLWMark(); 
        memClone._hwmScn = getHWMark();
        
        return memClone;
    }
}
