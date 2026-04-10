<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes" encoding="UTF-8"/>

    <!-- Template for root -->
    <xsl:template match="adoptions">
        <div class="adoptions-container">
            <h2>My Adoption Requests</h2>
            
            <xsl:choose>
                <xsl:when test="count(adoption) > 0">
                    <table class="adoptions-table">
                        <thead>
                            <tr>
                                <th>Request ID</th>
                                <th>Pet Name</th>
                                <th>Species</th>
                                <th>Breed</th>
                                <th>Request Date</th>
                                <th>Status</th>
                                <th>Appointment</th>
                            </tr>
                        </thead>
                        <tbody>
                            <xsl:apply-templates select="adoption"/>
                        </tbody>
                    </table>
                </xsl:when>
                <xsl:otherwise>
                    <div class="no-adoptions">
                        <p>You haven't submitted any adoption requests yet.</p>
                        <a href="PetListing/main.php" class="browse-link">Browse Pets to Adopt →</a>
                    </div>
                </xsl:otherwise>
            </xsl:choose>
        </div>
    </xsl:template>

    <!-- Template for each adoption -->
    <xsl:template match="adoption">
        <tr>
            <td><xsl:value-of select="adoptionID"/></td>
            <td><xsl:value-of select="petName"/></td>
            <td><xsl:value-of select="animal"/></td>
            <td><xsl:value-of select="breed"/></td>
            <td><xsl:value-of select="requestDate"/></td>
            <td>
                <xsl:attribute name="class">
                    <xsl:choose>
                        <xsl:when test="status = 'Pending'">status-pending</xsl:when>
                        <xsl:when test="status = 'Approved'">status-approved</xsl:when>
                        <xsl:when test="status = 'Rejected'">status-rejected</xsl:when>
                    </xsl:choose>
                </xsl:attribute>
                <xsl:value-of select="status"/>
            </td>
            <td>
                <xsl:choose>
                    <xsl:when test="appointmentDate != ''">
                        <xsl:value-of select="appointmentDate"/>
                    </xsl:when>
                    <xsl:otherwise>Not scheduled</xsl:otherwise>
                </xsl:choose>
            </td>
        </tr>
    </xsl:template>

</xsl:stylesheet>