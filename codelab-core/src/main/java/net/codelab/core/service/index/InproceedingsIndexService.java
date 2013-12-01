package net.codelab.core.service.index;

import net.codelab.core.entity.dto.Inproceedings;
import net.codelab.core.entity.dto.ResultsDTO;
import net.codelab.core.lucene.inproceedings.providers.InproceedingsDataProvider;

import java.io.IOException;

/**
 * Created by Melzarek on 01/12/13.
 */

public interface InproceedingsIndexService extends InproceedingsDataProvider {

    public ResultsDTO<Inproceedings> getInproceedingByTitle(String query) throws IOException;

}
