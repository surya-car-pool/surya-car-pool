# Surya Car Pool flow and sequence diagrams

These diagrams follow the current Java controllers and services. The README and Postman collection contain older ride-sharing routes and do not match these flows.

## 1. Customer booking and payment flow

![Customer booking and payment flow](customer-booking-flow.svg)

```mermaid
flowchart TD
    A[Sign in: email or phone] --> B[Browse active, AVAILABLE cars]
    B --> C[Select car and submit POST /bookings]
    C --> D[BookingServiceImpl saves ACTIVE booking]
    D --> E{Payment method}
    E -->|Cash| F[POST /payments/confirm]
    F --> G[Save Payment and mark car BOOKED]
    E -->|UPI / card-style UI| H[POST /api/payments/initiate]
    H --> I[POST /api/payments/verify with client success flag]
    I --> J[On success: save PaymentReceipt and mark car BOOKED]
    G --> K[GET /bookings/invoice/{id}]
    J --> K
    K --> L[InvoiceService generates PDF]
    M[Important: booking payment fields are not updated] -.-> D
```

## 2. Owner onboarding and administrator approval

![Owner onboarding and admin approval](owner-admin-flow.svg)

```mermaid
flowchart TD
    A[Owner enters profile, car, and document details] --> B[POST /api/owners multipart]
    B --> C[OwnerService stores uploads and saves owner plus car]
    C --> D[Owner account starts PENDING]
    D --> E[Administrator reviews owner]
    E --> F{Decision}
    F -->|Approve| G[Owner APPROVED and enabled]
    G --> H[Car may be listed when active and AVAILABLE]
    F -->|Reject| I[Owner REJECTED and disabled]
    I --> J[Owner cars excluded from browse query]
    K[Access caveat: this endpoint is not explicitly public] -.-> B
```

## 3. Authentication and booking sequence

![Authentication and booking sequence](booking-sequence.svg)

```mermaid
sequenceDiagram
    autonumber
    actor Browser
    participant Security as Spring Security
    participant Auth as CustomAuthenticationProvider
    participant Controller as MVC Controllers
    participant Service as Booking Service
    participant Repository as Repositories / MySQL

    Browser->>Security: POST /login (identifier, password)
    Security->>Auth: authenticate()
    Auth->>Repository: findByEmail, otherwise findByPhone
    Repository-->>Auth: User record or no match
    Auth-->>Security: Authenticated principal + role
    Security-->>Browser: Session and role-based redirect

    Browser->>Controller: GET /bookcar/ui
    Controller->>Repository: Find active car, enabled owner, AVAILABLE
    Repository-->>Controller: Available cars
    Controller-->>Browser: Car list
    Browser->>Controller: POST /bookings (BookingForm)
    Controller->>Service: createBookingFromForm(form, authenticated user)
    Service->>Repository: Save booking as ACTIVE
    Repository-->>Service: Saved booking
    Controller-->>Browser: Redirect to /payments/ui
```

## 4. Payment and invoice sequence

![Payment and invoice sequence](payment-sequence.svg)

```mermaid
sequenceDiagram
    autonumber
    actor Browser
    participant Controller as Payment Controllers
    participant Service as Payment / Car Services
    participant Repository as Payment / Receipt Repositories
    participant DB as Car / Booking Database

    alt Cash
        Browser->>Controller: POST /payments/confirm
        Controller->>Repository: Save Payment
        Controller->>Service: Mark car BOOKED
        Service->>DB: Update car status
        Controller-->>Browser: Payment receipt page
    else Mock UPI/card-style flow
        Browser->>Controller: POST /api/payments/initiate
        Controller->>Service: initiatePayment(...)
        Browser->>Controller: POST /api/payments/verify (client success flag)
        Controller->>Service: confirmPayment(...)
        Service->>Repository: Save PaymentReceipt on success
        Service->>DB: Mark car BOOKED
        Service-->>Browser: Payment result
    end
    Browser->>Controller: GET /bookings/invoice/{id} (separate follow-up)
    Controller->>Service: generateInvoicePdf(booking)
    Service->>DB: Load booking data
    Service-->>Browser: Generated PDF
```

## Source-linked caveats

- Booking is persisted as `ACTIVE` before payment. The payment handlers do not update the booking's `paymentConfirmed` or `paymentMethod` fields.
- The online flow is mock/client-driven; a client-supplied success value is not external payment verification.
- Payment initiation and some template actions reference endpoints or UI data that are missing/incomplete in the current repository.
- Owner submission endpoint access and owner password handling differ from the normal registration path.
