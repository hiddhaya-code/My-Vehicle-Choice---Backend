# MyVehicleChoice — Backend REST API Specification

See the master copy at [BACKEND_ENDPOINTS.md](file:///C:/Users/Mohamed%20hiddhaya/.gemini/antigravity/scratch/myvehiclechoice/BACKEND_ENDPOINTS.md).

Interactive Swagger OpenAPI UI is available at `http://localhost:8080/swagger-ui/index.html`.

## Endpoint Overview
- **Authentication:** `POST /api/v1/auth/login`, `POST /api/v1/auth/register`, `GET /api/v1/users/me`
- **Catalogue:** `GET /api/v1/brands`, `GET /api/v1/vehicles`, `GET /api/v1/vehicles/{id}`, `GET /api/v1/vehicles/trending`, `GET /api/v1/vehicles/featured`, `GET /api/v1/vehicles/compare-search`
- **Recommendation Engine:** `POST /api/v1/recommendations/evaluate`, `GET /api/v1/users/me/recommendations`
- **Calculators:** `POST /api/v1/calculators/emi`, `POST /api/v1/calculators/running-cost`, `POST /api/v1/calculators/ownership-cost`
- **Dealerships & Enquiries:** `GET /api/v1/dealers`, `POST /api/v1/enquiries`, `GET /api/v1/users/me/enquiries`
- **Customer Shortlists:** `GET /api/v1/users/wishlist`, `POST /api/v1/users/wishlist/{id}`, `DELETE /api/v1/users/wishlist/{id}`, `GET /api/v1/users/saved-comparisons`, `POST /api/v1/users/saved-comparisons`
- **Admin Management (`ROLE_ADMIN`):** `GET /api/v1/admin/stats`, `GET /api/v1/admin/enquiries`, `PATCH /api/v1/admin/enquiries/{id}/status`, `POST /api/v1/admin/vehicles`, `DELETE /api/v1/admin/vehicles/{id}`, `POST /api/v1/admin/import-csv`
