package jjoommnn.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
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

public class TestChart
{
    public static void main( String[] args ) throws Exception
    {
        CategoryDataset data = createDataset();
        JFreeChart chart = createChart( data );
        
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsJPEG( bOut, chart, 400, 300 );
        
        ByteArrayInputStream bIn = new ByteArrayInputStream( bOut.toByteArray() );
        
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage( page );
        
        PDXObjectImage image = new PDJpeg( doc, bIn );
        
        PDFont font = PDType1Font.HELVETICA_BOLD;
        
        PDPageContentStream content = new PDPageContentStream( doc, page );
        content.drawImage( image, 0, 300 );
        
        content.beginText();
        content.setFont( font, 12 );
        content.moveTextPositionByAmount( 100, 10 );
        content.drawString( "한글 Hello from www.printmyfolders.com" );
        content.endText();
        
        content.close();
        
        doc.save( "C:\\Temp\\ImageNowPdf.pdf" );
        doc.close();
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
