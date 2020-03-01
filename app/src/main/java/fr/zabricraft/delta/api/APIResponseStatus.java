package fr.zabricraft.delta.api;

import fr.zabricraft.delta.R;

public enum APIResponseStatus {

    ok, created, notFound, unauthorized, invalidRequest, offline, loading;

    public int text() {
        switch (this) {
            case notFound:
                return R.string.status_notFound;
            case unauthorized:
                return R.string.status_unauthorized;
            case invalidRequest:
                return R.string.status_invalidRequest;
            case offline:
                return R.string.status_offline;
            case loading:
                return R.string.status_loading;
        }
        return 0;
    }

}
