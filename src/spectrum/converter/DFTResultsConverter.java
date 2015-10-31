package spectrum.converter;

import java.util.concurrent.CopyOnWriteArrayList;

import spectrum.DFTResultsListener;
import spectrum.DFTResultsProvider;

public abstract class DFTResultsConverter 
			implements DFTResultsListener, DFTResultsProvider
{
	private CopyOnWriteArrayList<DFTResultsListener> mListeners = 
						new CopyOnWriteArrayList<DFTResultsListener>();
	
	/**
	 * DFT Results Converter - for converting the output of the JTransforms
	 * FFT library real and complex forward results
	 */
	public DFTResultsConverter()
	{
	}

	/**
	 * Specifies the sample size in bits to establish the maximum dynamic range.  
	 * All FFT bin values will be scaled according to this value.
	 */
	public abstract void setSampleSize( double size );
	
	public void dispose()
	{
		mListeners.clear();
	}

	@Override
    public void addListener( DFTResultsListener listener )
    {
		mListeners.add( listener );
    }

	@Override
    public void removeListener( DFTResultsListener listener )
    {
		mListeners.remove( listener );
    }

	protected void dispatch( float[] results )
	{
		for( DFTResultsListener listener: mListeners )
		{
			listener.receive( results );
		}
	}
}
