package jjoommnn.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class TestChart
{

    public static void main( String[] args ) throws Exception
    {
        System.out.println("Images");
        
        CategoryDataset data = createDataset();
        JFreeChart chart = createChart( data );
        
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsJPEG( bOut, chart, 400, 300 );
        
        // step 1: creation of a document-object
        Document document = new Document();
        
        try {
            // step 2:
            // we create a writer that listens to the document
            // and directs a PDF-stream to a file
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Temp\\Images.pdf"));
            
            // step 3: we open the document
            document.open();
            
            // step 4:
            document.add(new Paragraph("A picture of my dog: otsoe.jpg"));
            Image jpg = Image.getInstance( bOut.toByteArray() );
            document.add(jpg);
        }
        catch(DocumentException de) {
            System.err.println(de.getMessage());
        }
        catch(IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        
        // step 5: we close the document
        document.close();
    }
    
    private static CategoryDataset createDataset()
    {
        // row keys...
        final String series1 = "한글";
        final String series2 = "Second";
        final String series3 = "Third";

        // column keys...
        final String category1 = "한글 1";
        final String category2 = "Category 2";
        final String category3 = "Category 3";
        final String category4 = "Category 4";
        final String category5 = "Category 5";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue( 1.0, series1, category1 );
        dataset.addValue( 4.0, series1, category2 );
        dataset.addValue( 3.0, series1, category3 );
        dataset.addValue( 5.0, series1, category4 );
        dataset.addValue( 5.0, series1, category5 );

        dataset.addValue( 5.0, series2, category1 );
        dataset.addValue( 7.0, series2, category2 );
        dataset.addValue( 6.0, series2, category3 );
        dataset.addValue( 8.0, series2, category4 );
        dataset.addValue( 4.0, series2, category5 );

        dataset.addValue( 4.0, series3, category1 );
        dataset.addValue( 3.0, series3, category2 );
        dataset.addValue( 2.0, series3, category3 );
        dataset.addValue( 3.0, series3, category4 );
        dataset.addValue( 6.0, series3, category5 );

        return dataset;
    }

    private static JFreeChart createChart( final CategoryDataset dataset )
    {
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart( "Bar Chart 한글", // chart
                                                                                // title
                "Category", // domain axis label
                "Value", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
                );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint( Color.white );

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint( Color.lightGray );
        plot.setDomainGridlinePaint( Color.white );
        plot.setRangeGridlinePaint( Color.white );
        
        Font font = plot.getDomainAxis().getLabelFont();
        // X축 라벨
        plot.getDomainAxis().setLabelFont(new Font("돋움", font.getStyle(), font.getSize()));
        // X축 도메인
        plot.getDomainAxis().setTickLabelFont(new Font("돋움", font.getStyle(), 10));

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
        rangeAxis.setStandardTickUnits( NumberAxis.createIntegerTickUnits() );

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer)plot.getRenderer();
        renderer.setDrawBarOutline( false );

        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint( 0.0f, 0.0f, Color.blue, 0.0f, 0.0f, Color.lightGray );
        final GradientPaint gp1 = new GradientPaint( 0.0f, 0.0f, Color.green, 0.0f, 0.0f, Color.lightGray );
        final GradientPaint gp2 = new GradientPaint( 0.0f, 0.0f, Color.red, 0.0f, 0.0f, Color.lightGray );
        renderer.setSeriesPaint( 0, gp0 );
        renderer.setSeriesPaint( 1, gp1 );
        renderer.setSeriesPaint( 2, gp2 );

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions( CategoryLabelPositions.createUpRotationLabelPositions( Math.PI / 6.0 ) );
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;
    }

}
