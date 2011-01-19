/*
************************************************************************
*******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
**************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
*
*  (c) 2009.                            (c) 2009.
*  Government of Canada                 Gouvernement du Canada
*  National Research Council            Conseil national de recherches
*  Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
*  All rights reserved                  Tous droits réservés
*                                       
*  NRC disclaims any warranties,        Le CNRC dénie toute garantie
*  expressed, implied, or               énoncée, implicite ou légale,
*  statutory, of any kind with          de quelque nature que ce
*  respect to the software,             soit, concernant le logiciel,
*  including without limitation         y compris sans restriction
*  any warranty of merchantability      toute garantie de valeur
*  or fitness for a particular          marchande ou de pertinence
*  purpose. NRC shall not be            pour un usage particulier.
*  liable in any event for any          Le CNRC ne pourra en aucun cas
*  damages, whether direct or           être tenu responsable de tout
*  indirect, special or general,        dommage, direct ou indirect,
*  consequential or incidental,         particulier ou général,
*  arising from the use of the          accessoire ou fortuit, résultant
*  software.  Neither the name          de l'utilisation du logiciel. Ni
*  of the National Research             le nom du Conseil National de
*  Council of Canada nor the            Recherches du Canada ni les noms
*  names of its contributors may        de ses  participants ne peuvent
*  be used to endorse or promote        être utilisés pour approuver ou
*  products derived from this           promouvoir les produits dérivés
*  software without specific prior      de ce logiciel sans autorisation
*  written permission.                  préalable et particulière
*                                       par écrit.
*                                       
*  This file is part of the             Ce fichier fait partie du projet
*  OpenCADC project.                    OpenCADC.
*                                       
*  OpenCADC is free software:           OpenCADC est un logiciel libre ;
*  you can redistribute it and/or       vous pouvez le redistribuer ou le
*  modify it under the terms of         modifier suivant les termes de
*  the GNU Affero General Public        la “GNU Affero General Public
*  License as published by the          License” telle que publiée
*  Free Software Foundation,            par la Free Software Foundation
*  either version 3 of the              : soit la version 3 de cette
*  License, or (at your option)         licence, soit (à votre gré)
*  any later version.                   toute version ultérieure.
*                                       
*  OpenCADC is distributed in the       OpenCADC est distribué
*  hope that it will be useful,         dans l’espoir qu’il vous
*  but WITHOUT ANY WARRANTY;            sera utile, mais SANS AUCUNE
*  without even the implied             GARANTIE : sans même la garantie
*  warranty of MERCHANTABILITY          implicite de COMMERCIALISABILITÉ
*  or FITNESS FOR A PARTICULAR          ni d’ADÉQUATION À UN OBJECTIF
*  PURPOSE.  See the GNU Affero         PARTICULIER. Consultez la Licence
*  General Public License for           Générale Publique GNU Affero
*  more details.                        pour plus de détails.
*                                       
*  You should have received             Vous devriez avoir reçu une
*  a copy of the GNU Affero             copie de la Licence Générale
*  General Public License along         Publique GNU Affero avec
*  with OpenCADC.  If not, see          OpenCADC ; si ce n’est
*  <http://www.gnu.org/licenses/>.      pas le cas, consultez :
*                                       <http://www.gnu.org/licenses/>.
*
*  $Revision: 4 $
*
************************************************************************
*/

package ca.nrc.cadc.vos.server.web.representation;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.restlet.data.MediaType;
import org.restlet.representation.OutputRepresentation;

import ca.nrc.cadc.date.DateUtil;
import ca.nrc.cadc.vos.ContainerNode;
import ca.nrc.cadc.vos.DataNode;
import ca.nrc.cadc.vos.Node;
import ca.nrc.cadc.vos.NodeProperty;
import ca.nrc.cadc.vos.NodeWriter;
import ca.nrc.cadc.vos.VOS;
import java.text.DateFormat;

/**
 * Creates an XML representation of a Node
 * 
 * @author majorb
 *
 */
public class NodeOutputRepresentation extends OutputRepresentation
{
    protected static Logger LOGGER =
            Logger.getLogger(NodeOutputRepresentation.class);
    
    private Node node;
    private NodeWriter nodeWriter;


    public NodeOutputRepresentation(Node node, NodeWriter nodeWriter)
    {
        super(MediaType.TEXT_XML);
        this.node = node;
        this.nodeWriter = nodeWriter;
    }


    @Override
    public void write(final OutputStream outputStream) throws IOException
    {
        final Node n = getNode();

        if (n instanceof DataNode)
        {
            getNodeWriter().write((DataNode) n, outputStream);
        }
        else if (n instanceof ContainerNode)
        {
            getNodeWriter().write((ContainerNode) n, outputStream);
        }
    }
    
    @Override
    public Date getModificationDate()
    {
        final NodeProperty modificationDate =
                getNode().findProperty(VOS.PROPERTY_URI_DATE);

        if (modificationDate != null)
        {
            try
            {
                final DateFormat df =
                        DateUtil.getDateFormat(DateUtil.IVOA_DATE_FORMAT,
                                               DateUtil.UTC);
                return df.parse(modificationDate.getPropertyValue());
            }
            catch (ParseException e)
            {
                LOGGER.warn("Date " + modificationDate.getPropertyValue()
                            + " could not be parsed.");
            }
        }

        return null;
    }


    public NodeWriter getNodeWriter()
    {
        return nodeWriter;
    }

    public Node getNode()
    {
        return node;
    }
}
