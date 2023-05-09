package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class gscCheckBasicAttr {

    public static boolean gscCheckPSBasicAttr(String Attr) {

        List<String> ProjectSpaceBasicAttrlist = new ArrayList<>(Arrays.asList(
                "contenturl",                "fullcontenturl",                "module",                "attachedbranch",                "branch",
                "iteration",                "name",                "description",                "shortdescription",                "revision",
                "originated",                "modified",                "lattice",                "owner",                "grant",
                "grantor",                "grantee",                "granteeaccess",                "granteesignature",                "grantkey",
                "policy",                "type",                "attribute",                "default",                "current",
                "state",                "revisions",                "previous",                "next",                "first",
                "last",                "latest",                "islast",                "history",                "relationship",
                "to",                "from",                "member",                "toset",                "fromset",
                "exists",                "islockingenforced",                "vault",                "locked",                "locker",
                "id",                "method",                "context",                "index",                "interface",
                "interface",                "expression",                "evaluate",                "reserved",                "reservedby",
                "reservedcomment",                "reservedstart",                "vcfile",                "vcfolder",                "vcmodule",
                "organization",                "project",                "revindex",                "ownership",                "inheritedownership",
                "access",                "physicalid",                "logicalid",                "majorid",                "cestamp",
                "updatestamp",                "minorrevision",                "minorrevisions",                "minororder",                "previousminor",
                "nextminor",                "firstminor",                "lastminor",                "ispublished",                "lastpublished",
                "isbestsofar",                "bestsofar",                "majorrevision",                "majororder",                "majorids",
                "versionid",                "dov",                "iov",                "paths",                "iscomposee",                "composer",
                "proxy",                "transaction",                "immutable",                "isinmodule"
        ));

        boolean CheckBasicAttr = ProjectSpaceBasicAttrlist.contains(Attr);

        return CheckBasicAttr;
    }
}
